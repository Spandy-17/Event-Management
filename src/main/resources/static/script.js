function showSignup() {
    document.getElementById("loginBox").classList.add("hidden");
    document.getElementById("signupBox").classList.remove("hidden");
}

function showLogin() {
    document.getElementById("signupBox").classList.add("hidden");
    document.getElementById("loginBox").classList.remove("hidden");
}

function signup() {
    const username = document.getElementById("signupUsername").value.trim();
    const password = document.getElementById("signupPassword").value;
    const confirmPassword = document.getElementById("signupConfirmPassword").value;
    const department = document.getElementById("signupDepartment").value;
    const error = document.getElementById("signupError");

    error.textContent = "";

    if (!username || !password || !confirmPassword || !department) {
        error.textContent = "All fields are required";
        return;
    }

    if (password.length < 8) {
        error.textContent = "Password must be at least 8 characters";
        return;
    }

    if (password !== confirmPassword) {
        error.textContent = "Passwords do not match";
        return;
    }

    fetch("http://192.168.1.8:8080/api/students/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            name: username,
            password: password,
            email: username + "@college.com",
            rollNumber: "TEMP123",
            department: department
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Signup failed");
        return res.text();
    })
    .then(() => {
        alert("Signup successful");
        showLogin();
    })
    .catch(() => {
        error.textContent = "Signup failed. Please try again.";
    });
}

function login() {
    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value;
    const error = document.getElementById("loginError");

    error.textContent = "";

    if (!username || !password) {
        error.textContent = "Please enter username and password";
        return;
    }

    if (password.length < 8) {
        error.textContent = "Invalid password";
        return;
    }

    fetch("http://192.168.1.8:8080/api/students/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: username, password: password })
    })
    .then(res => res.json())
    .then(data => {
        if (!data || !data.id) {
            error.textContent = "User not found. Please signup first.";
            return;
        }
        localStorage.setItem("username", data.name);
        window.location.href = "dashboard.html";
    })
    .catch(() => {
        error.textContent = "Login failed. Check your credentials.";
    });
}
