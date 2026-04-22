const toggleBtn = document.getElementById("themeToggle");

toggleBtn.addEventListener("click", () => {
    document.body.classList.toggle("light");
    document.body.classList.toggle("dark");
    toggleBtn.textContent = document.body.classList.contains("light") ? "🌞" : "🌙";
});

function goToCategory(category) {
    window.location.href = `events.html?category=${category}`;
}

function toggleProfileMenu() {
    const menu = document.getElementById("profileMenu");
    menu.style.display = menu.style.display === "block" ? "none" : "block";
}

document.addEventListener("click", function(e) {
    const container = document.querySelector(".profile-container");
    const menu = document.getElementById("profileMenu");
    if (container && !container.contains(e.target)) {
        menu.style.display = "none";
    }
});

function selectRole(role) {
    localStorage.setItem("role", role);
    const adminPanel = document.getElementById("adminPanel");
    const scanBtn = document.getElementById("scanBtn");

    if (role === "admin") {
        adminPanel.style.display = "block";
        scanBtn.style.display = "none";
    } else {
        adminPanel.style.display = "none";
        scanBtn.style.display = "inline-flex";
    }
    document.getElementById("profileMenu").style.display = "none";
}

function formatDateTime(isoStr) {
    if (!isoStr) return "TBA";
    const d = new Date(isoStr);
    return d.toLocaleString("en-IN", {
        day: "numeric", month: "short", year: "numeric",
        hour: "2-digit", minute: "2-digit", hour12: true
    });
}

function previewFile(input, previewId, boxId) {
    const preview = document.getElementById(previewId);
    const box = document.getElementById(boxId);
    if (input.files && input.files[0]) {
        const file = input.files[0];
        if (file.size > 5 * 1024 * 1024) {
            alert("File size must be under 5MB");
            input.value = "";
            return;
        }
        if (file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = e => {
                preview.src = e.target.result;
                preview.style.display = "block";
            };
            reader.readAsDataURL(file);
        } else {
            preview.style.display = "none";
        }
        box.classList.add("has-file");
        box.querySelector(".upload-text").textContent = file.name;
    }
}

function goToMyAccount() {
    document.getElementById("profileMenu").style.display = "none";
    window.location.href = "my-account.html";
}

function postEvent() {
    const theme = document.getElementById("eventTheme").value;
    const name = document.getElementById("eventName").value.trim();
    const venue = document.getElementById("eventVenue").value.trim();
    const desc = document.getElementById("eventDesc").value.trim();
    const startDate = document.getElementById("eventStartDate").value;
    const endDate = document.getElementById("eventEndDate").value;

    if (!theme || !name || !desc || !venue || !startDate || !endDate) {
        alert("Please fill all required fields");
        return;
    }

    const formData = new FormData();

    formData.append("eventName", name);
    formData.append("location", theme);   // ✅ FIXED
    formData.append("venue", venue);
    formData.append("description", desc);
    formData.append("startTime", startDate);
    formData.append("endTime", endDate);
    formData.append("createdBy", localStorage.getItem("username"));

    fetch("http://localhost:8080/api/events/create", {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(data => {
        alert("✅ Event posted successfully");
        location.reload();
    })
    .catch(err => {
        console.error(err);
        alert("❌ Failed to post event");
    });
}
async function generateQR(eventId) {
    const res = await fetch(`http://192.168.1.8:8080/api/qr/generate/${eventId}`, {
        method: "POST"
    });
    const data = await res.json();
    QRCode.toCanvas(document.getElementById("qrCanvas"), data.qrToken);
}

async function registerEvent(eventName) {
    const username = localStorage.getItem("username");
    if (!username) { alert("Please login first"); return; }

    const res = await fetch("http://192.168.1.8:8080/api/attendance/mark", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: username, token: eventName })
    });

    if (res.ok) {
        alert("Successfully registered for the event");
    } else {
        alert("Already registered or error");
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

let qrScanner;

function openScanner() {
    document.getElementById("scannerPanel").style.display = "flex";
    qrScanner = new Html5Qrcode("reader");
    qrScanner.start(
        { facingMode: "environment" },
        { fps: 10, qrbox: 250 },
        qrCodeMessage => { handleScannedToken(qrCodeMessage); }
    );
}

function closeScanner() {
    if (qrScanner) qrScanner.stop();
    document.getElementById("scannerPanel").style.display = "none";
}

function handleScannedToken(token) {
    const username = localStorage.getItem("username");
    if (!username) { alert("Please login first"); return; }

    fetch("http://192.168.1.8:8080/api/attendance/mark", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ UserName: username, token: token })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("Attendance marked successfully!");
        closeScanner();
    })
    .catch(() => {
        alert("Attendance already marked or error");
    });
}

window.onload = function () {
    const role = localStorage.getItem("role");
    if (role === "student") {
        document.getElementById("scanBtn").style.display = "inline-flex";
        document.getElementById("adminPanel").style.display = "none";
    } else if (role === "admin") {
        document.getElementById("scanBtn").style.display = "none";
        document.getElementById("adminPanel").style.display = "block";
    }
};

function deleteEvent(eventId) {
    if (!confirm("Delete this event?")) return;

    fetch(`http://localhost:8080/api/events/delete/${eventId}`, {
        method: "DELETE"
    })
    .then(res => {
        if (res.ok) {
            alert("✅ Event deleted successfully");
            location.reload();
        } else {
            alert("❌ Failed to delete event");
        }
    })
    .catch(err => {
        console.error(err);
        alert("❌ Delete failed");
    });
}
