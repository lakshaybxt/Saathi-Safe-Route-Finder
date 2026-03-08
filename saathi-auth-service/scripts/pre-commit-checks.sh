#!/usr/bin/env bash
set -euo pipefail

echo "Running pre-commit best-practices checks..."

# get staged files
staged_files=$(git diff --cached --name-only --diff-filter=ACM || true)
if [ -z "$staged_files" ]; then
  echo "No staged files to check."
else
  echo "Staged files:"
  echo "$staged_files"
fi

# Run Maven unit tests (skip if mvn not available)
if command -v mvn >/dev/null 2>&1; then
  echo "Running mvn test..."
  mvn -q -DskipTests=false test
else
  echo "mvn not found; skipping mvn tests."
fi

# Determine which static analysis goals are configured in pom.xml
if command -v mvn >/dev/null 2>&1 && [ -f pom.xml ]; then
  echo "Checking configured static-analysis plugins in pom.xml..."
  goals=()
  if grep -qE '<artifactId>\s*maven-checkstyle-plugin\s*<\/artifactId>' pom.xml; then
    goals+=("checkstyle:check")
  fi
  if grep -qE '<artifactId>\s*maven-pmd-plugin\s*<\/artifactId>' pom.xml || grep -qE '<artifactId>\s*pmd-maven-plugin\s*<\/artifactId>' pom.xml; then
    goals+=("pmd:check")
  fi
  if grep -qE '<artifactId>\s*spotbugs-maven-plugin\s*<\/artifactId>' pom.xml; then
    goals+=("spotbugs:check")
  fi

  if [ "${#goals[@]}" -eq 0 ]; then
    echo "No checkstyle/pmd/spotbugs plugins found in pom.xml; skipping static analysis."
  else
    echo "Running static analysis goals: ${goals[*]}"
    mvn -q "${goals[@]}" || {
      echo "Static analysis failed. Fix issues reported by the plugins."
      exit 1
    }
  fi
fi

# Patterns that should fail a commit
declare -a PATTERNS=(
  "System\.out\.print"
  "printStackTrace\("
  "TODO"
  "FIXME"
  "password\s*="
  "secret\s*="
  "AKIA[0-9A-Z]{16}"
)

fail=0

for file in $staged_files; do
  case "$file" in
    *.java|*.properties|*.yml|*.yaml|*.xml|*.gradle|*.env)
      if git show :"$file" >/dev/null 2>&1; then
        content=$(git show :"$file")
        for p in "${PATTERNS[@]}"; do
          if echo "$content" | grep -nE "$p" >/dev/null 2>&1; then
            echo "ERROR: banned pattern '$p' found in $file"
            echo "$content" | grep -nE --color=always "$p" || true
            fail=1
          fi
        done
      fi
    ;;
    *)
      ;;
  esac
done

if [ "$fail" -ne 0 ]; then
  echo "Pre-commit checks failed. Remove hardcoded secrets/banned patterns and re-run."
  exit 1
fi

echo "All pre-commit checks passed."

# --- JaCoCo coverage check (bash) ---
JACOCO_XML="target/site/jacoco/jacoco.xml"
THRESHOLD=75
if [ -f "$JACOCO_XML" ]; then
  lineCounter=$(grep -oE '<counter type="LINE"[^>]+>' "$JACOCO_XML" | head -n 1 || true)
  if [ -n "$lineCounter" ]; then
    covered=$(echo "$lineCounter" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')
    missed=$(echo "$lineCounter" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
    total=$((covered + missed))
    if [ "$total" -eq 0 ]; then
      echo "ERROR: No executable lines found in JaCoCo report - likely no tests were executed." >&2
      exit 1
    fi
    # compute percentage with awk for float precision
    coverage=$(awk "BEGIN {printf \"%.2f\", ($covered/($total))*100}")
    echo "Code Coverage: ${coverage}%"
    if (( $(echo "$coverage < $THRESHOLD" | bc -l) )); then
      echo "Code coverage below threshold ($THRESHOLD%). Current: ${coverage}%" >&2
      exit 1
    else
      echo "Code coverage above threshold. Current: ${coverage}%"
    fi
  else
    echo "ERROR: Could not find LINE coverage data in JaCoCo report." >&2
    exit 1
  fi
else
  echo "ERROR: JaCoCo report not found at $JACOCO_XML. Ensure tests and coverage plugin are configured correctly." >&2
  exit 1
fi

# End of script
