document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault(); 

  const email = e.target.email.value.trim();
  const password = e.target.password.value;

  const requestBody = {
    email: email,
    password: password
  };

  fetch("https://your-backend-api.com/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(requestBody)
  })
  .then(async response => {
    const data = await response.json();

    if (response.ok) {
      const token = data["token"];
      const expiration = data["expiration"];

      console.log("Login successful");
      console.log("Token:", token);
      console.log("Expiration:", expiration);

      localStorage.setItem("authToken", token);

      window.location.href = "dashboard.html";
    } else if (response.status === 400 || response.status === 404) {
      alert(data.message || "Something went wrong. Please try again.");
    } else {
      alert("Unexpected error occurred.");
    }
  })
  .catch(error => {
    console.error("Fetch error:", error);
    alert("Network error. Please check your connection.");
  });
});
