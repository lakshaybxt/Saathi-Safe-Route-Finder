document.getElementById("registerForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const email = e.target.email.value.trim();
  const verificationCode = e.target.code.value.trim();
  const username = e.target.username.value.trim();

  const password = "securePassword";

  try {
    
    const registerResponse = await fetch("https://your-backend-api.com/api/v1/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password, username })
    });

    const registerData = await registerResponse.json();

    if (!registerResponse.ok) {
      throw new Error(registerData.message || "Registration failed.");
    }

    console.log("✅ Registration success:", registerData);

   
    const verifyResponse = await fetch("https://your-backend-api.com/api/v1/auth/verify", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, verificationCode })
    });

    const verifyText = await verifyResponse.text();

    if (verifyResponse.ok) {
      alert("✅ " + verifyText); 
      window.location.href = "index.html"; 
    } else {
      alert("❌ Verification failed: " + verifyText);
    }

  } catch (err) {
    console.error("❌ Error:", err);
    alert("❌ " + err.message);
  }
});
