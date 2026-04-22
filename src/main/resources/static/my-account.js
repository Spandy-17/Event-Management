const toggleBtn = document.getElementById("themeToggle");
toggleBtn.addEventListener("click", () => {
    document.body.classList.toggle("light");
    document.body.classList.toggle("dark");
    toggleBtn.textContent = document.body.classList.contains("light") ? "🌞" : "🌙";
});

const username = localStorage.getItem("username") || "User";
document.getElementById("displayName").textContent = username;
document.getElementById("displayEmail").textContent = username + "@eventverse.com";
document.getElementById("avatarCircle").textContent = username.charAt(0).toUpperCase();

let allUserEvents = [];
let allBackendEvents = [];
let currentTab = "registered";

function formatDateTime(isoStr) {
    if (!isoStr) return "TBA";
    const d = new Date(isoStr);
    return d.toLocaleString("en-IN", {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        hour12: true
    });
}

async function loadUserEvents() {
    const spinner = document.getElementById("loadingSpinner");
    spinner.style.display = "block";

    try {
        const regRes = await fetch(`http://192.168.1.8:8080/api/registrations/user/${username}`);
        if (regRes.ok) {
            allUserEvents = await regRes.json();
            console.log("Registrations:", allUserEvents);
        }

        const eventsRes = await fetch("http://192.168.1.8:8080/api/events/all");
        if (eventsRes.ok) {
            allBackendEvents = await eventsRes.json();
            console.log("Events:", allBackendEvents);
        }

    } catch (e) {
        console.error("Failed loading:", e);
    }

    spinner.style.display = "none";
    updateStats();
    renderEvents();
}

function normalize(str) {
    return (str || "").trim().toLowerCase();
}

function getMatchedEvents() {
    const registeredNames = allUserEvents.map(r => normalize(r.eventName));
    const now = new Date();

    const matched = allBackendEvents.filter(ev =>
        registeredNames.includes(normalize(ev.eventName))
    );

    const registered = matched.filter(ev => {
        const end = ev.endTime ? new Date(ev.endTime) : null;
        return !end || end > now;
    });

    const completed = matched.filter(ev => {
        const end = ev.endTime ? new Date(ev.endTime) : null;
        return end && end <= now;
    });

    return { registered, completed };
}

async function getOrganizedEvents() {
    try {
        const res = await fetch(`http://192.168.1.8:8080/api/events/organized/${username}`);
        if (res.ok) return await res.json();
    } catch (e) {
        console.error(e);
    }
    return [];
}

async function updateStats() {
    const { registered, completed } = getMatchedEvents();
    const organized = await getOrganizedEvents();

    document.getElementById("statRegistered").textContent = registered.length + completed.length;
    document.getElementById("statCompleted").textContent = completed.length;
    document.getElementById("statUpcoming").textContent = registered.length;
    document.getElementById("statOrganized").textContent = organized.length;
}

function switchTab(tab) {
    currentTab = tab;

    document.querySelectorAll(".tab-btn").forEach(btn => btn.classList.remove("active"));

    if (tab === "registered") document.querySelectorAll(".tab-btn")[0].classList.add("active");
    if (tab === "completed") document.querySelectorAll(".tab-btn")[1].classList.add("active");
    if (tab === "organized") document.querySelectorAll(".tab-btn")[2].classList.add("active");

    renderEvents();
}

async function renderEvents() {
    const container = document.getElementById("eventsListContainer");
    const { registered, completed } = getMatchedEvents();

    let events = [];

    if (currentTab === "registered") {
        events = registered;
    } else if (currentTab === "completed") {
        events = completed;
    } else {
        events = await getOrganizedEvents();
    }

    container.innerHTML = "";

    if (events.length === 0) {
        const icons = {
            registered: "📋",
            completed: "✅",
            organized: "🎯"
        };

        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">${icons[currentTab]}</div>
                <p>No ${currentTab} events found</p>
            </div>
        `;
        return;
    }

    events.forEach(ev => {
        const row = document.createElement("div");
        row.className = "event-row";

        let badgeClass = "upcoming";
        let badgeText = "Upcoming";
        let iconBg = "upcoming";
        let icon = "📅";

        if (currentTab === "completed") {
            badgeClass = "completed";
            badgeText = "Completed";
            iconBg = "completed";
            icon = "✅";
        }

        if (currentTab === "organized") {
            badgeClass = "organized";
            badgeText = "Organized";
            iconBg = "organized";
            icon = "🎯";
        }

        const now = new Date();
        const end = ev.endTime ? new Date(ev.endTime) : null;

        let extraButtons = "";

        if (currentTab === "organized") {
            if (end && end > now) {
                extraButtons += `
                    <button class="qr-btn" onclick="generateQR(${ev.id})">Generate QR</button>
                    <div id="qr-${ev.id}" style="margin-top:10px;"></div>
                `;
            }

			extraButtons += `
			    <button class="delete-btn" onclick="deleteEvent('${ev.eventName}', this.closest('.event-row'))">
			        Delete Event
			    </button>
			`;
        }

        if (currentTab === "completed") {
            extraButtons += `
                <button class="qr-btn" onclick="generateCertificate('${ev.eventName}')">
                    Generate Certificate
                </button>
            `;
        }

        row.innerHTML = `
            <div class="event-row-icon ${iconBg}">${icon}</div>
            <div class="event-row-info">
                <h4>${ev.eventName}</h4>
                <p>📍 ${ev.venue || "TBA"} · 🕐 ${formatDateTime(ev.startTime)} → ${formatDateTime(ev.endTime)}</p>
            </div>
            <span class="event-row-badge ${badgeClass}">${badgeText}</span>
            ${extraButtons}
        `;

        container.appendChild(row);
    });
}

function generateQR(eventId) {
    const qrBox = document.getElementById("qr-" + eventId);

    if (qrBox.innerHTML !== "") {
        qrBox.innerHTML = "";
        return;
    }

    qrBox.innerHTML = `
        <img src="http://192.168.1.8:8080/api/qr/generate/${eventId}"
             width="150"
             height="150"
             style="margin-top:10px; border-radius:8px; background:white; padding:6px;">
    `;
}

function generateCertificate(eventName) {
    alert("Certificate generation for " + eventName + " will be connected next");
}

async function deleteEvent(eventName, rowElement) {
    try {
        const res = await fetch(`http://192.168.1.8:8080/api/events/delete/${encodeURIComponent(eventName)}`, {
            method: "DELETE"
        });

        const msg = await res.text();
        alert(msg);

        rowElement.remove();

        loadUserEvents();

    } catch (e) {
        console.error("Delete failed:", e);
        alert("Delete failed");
    }
}

loadUserEvents();