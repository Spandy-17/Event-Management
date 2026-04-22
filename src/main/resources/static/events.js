const eventsData = {
    cultural: {
        events: [
            { name: "Dance Fest", desc: "Traditional and modern dance performances.", date: "2026-02-10", venue: "Main Auditorium", fee: "Free", organizer: "Cultural Club" },
            { name: "Drama Night", desc: "Stage plays and skits.", date: "2026-02-12", venue: "Open Air Theatre", fee: "₹100", organizer: "Drama Society" }
        ]
    },
    technical: {
        events: [
            { name: "Hackathon", desc: "24-hour coding challenge.", date: "2026-03-01", venue: "Innovation Lab", fee: "₹200", organizer: "Tech Club" },
            { name: "Tech Quiz", desc: "Test your technical knowledge.", date: "2026-03-03", venue: "Seminar Hall", fee: "Free", organizer: "CSI Chapter" }
        ]
    },
    "non-technical": {
        events: [
            { name: "Debate", desc: "Express your ideas confidently.", date: "2026-02-15", venue: "Conference Hall", fee: "Free", organizer: "Literary Club" },
            { name: "Quiz Bee", desc: "Fun general knowledge quiz.", date: "2026-02-16", venue: "Room A-204", fee: "₹50", organizer: "Quiz Club" }
        ]
    },
    sports: {
        events: [
            { name: "Football", desc: "Inter-college tournament.", date: "2026-01-25", venue: "Football Ground", fee: "Free", organizer: "Sports Committee" },
            { name: "Cricket", desc: "Knockout league matches.", date: "2026-01-28", venue: "Cricket Stadium", fee: "₹300", organizer: "Sports Committee" }
        ]
    },
    arts: {
        events: [
            { name: "Painting", desc: "Canvas art competition.", date: "2026-02-05", venue: "Art Room", fee: "₹100", organizer: "Fine Arts Club" },
            { name: "Craft Expo", desc: "Handmade artwork display.", date: "2026-02-06", venue: "Exhibition Hall", fee: "Free", organizer: "Creative Cell" }
        ]
    },
    music: {
        events: [
            { name: "Battle of Bands", desc: "Band vs band music battle.", date: "2026-02-20", venue: "Main Stage", fee: "₹150", organizer: "Music Club" },
            { name: "Solo Singing", desc: "Individual vocal performances.", date: "2026-02-21", venue: "Mini Auditorium", fee: "Free", organizer: "Music Club" }
        ]
    },
    workshops: {
        events: [
            { name: "Web Dev Workshop", desc: "Frontend & backend basics.", date: "2026-03-10", venue: "Computer Lab 1", fee: "₹500", organizer: "Developer Community" },
            { name: "AI Workshop", desc: "Intro to artificial intelligence.", date: "2026-03-12", venue: "AI Lab", fee: "₹700", organizer: "AI Club" }
        ]
    },
    others: {
        events: [
            { name: "Open Mic", desc: "Sing, speak or perform.", date: "2026-02-18", venue: "Cafeteria Stage", fee: "Free", organizer: "Student Council" },
            { name: "Fun Games", desc: "Relax and enjoy.", date: "2026-02-19", venue: "Activity Zone", fee: "Free", organizer: "Recreation Club" }
        ]
    }
};

const params = new URLSearchParams(window.location.search);
const category = params.get("category");
const title = document.getElementById("categoryTitle");
const container = document.getElementById("eventsContainer");

function formatDateTime(isoStr) {
    if (!isoStr) return "TBA";
    const d = new Date(isoStr);
    return d.toLocaleString("en-IN", {
        day: "numeric", month: "short", year: "numeric",
        hour: "2-digit", minute: "2-digit", hour12: true
    });
}

function createEventCard(event, category, isBackend) {
    const card = document.createElement("div");
    card.className = "event-card";

    const loggedRole = localStorage.getItem("role");
    const loggedUser = localStorage.getItem("username");
    const eventName = isBackend ? event.eventName : event.name;

    const venue = isBackend ? (event.venue || "To be announced") : event.venue;
    const startTime = isBackend ? formatDateTime(event.startTime) : event.date;
    const endTime = isBackend ? formatDateTime(event.endTime) : null;

	const posterUrl = isBackend && event.poster 
	    ? `http://192.168.1.8:8080/api/events/poster/${event.poster}` 
	    : null;

    card.innerHTML = `
        ${posterUrl ? `<div class="event-poster"><img src="${posterUrl}" alt="${eventName} poster" onerror="this.parentElement.style.display='none'"></div>` : ""}
        <div class="event-card-body">
        <h3>${eventName}</h3>
        <p class="event-desc">${isBackend ? (event.description || "Posted by admin") : event.desc}</p>
        <div class="event-meta">📍 <strong>Venue:</strong> ${venue}</div>
        <div class="event-meta">🕐 <strong>Start:</strong> ${startTime}</div>
        ${endTime ? `<div class="event-meta">🕑 <strong>End:</strong> ${endTime}</div>` : ""}
        <div class="event-meta">👤 <strong>Organizer:</strong> ${isBackend ? event.createdBy : event.organizer}</div>
        ${!isBackend ? `<div class="event-meta">💰 <strong>Fee:</strong> ${event.fee}</div>` : ""}
        <button class="register-btn" onclick="registerEvent('${category}', '${eventName}')">Register Now</button>
        ${loggedRole === "admin" && isBackend ? `<button class="qr-btn" onclick="generateQR('${category}', '${eventName}')">🔲 Generate QR</button><canvas id="qrCanvas" style="margin-top:8px;"></canvas>` : ""}
        </div>
    `;

    return card;
}

if (!category || !eventsData[category]) {
    title.textContent = "Events";
} else {
    title.textContent = category.replace("-", " ").toUpperCase() + " EVENTS";

    eventsData[category].events.forEach(event => {
        container.appendChild(createEventCard(event, category, false));
    });

    fetch("http://192.168.1.8:8080/api/events/all")
        .then(res => res.json())
        .then(data => {
            const seen = new Set();
            data.filter(ev => ev.location && ev.location.toLowerCase() === category.toLowerCase())
                .forEach(event => {
                    const key = event.eventName + "|" + event.createdBy;
                    if (!seen.has(key)) {
                        seen.add(key);
                        container.appendChild(createEventCard(event, category, true));
                    }
                });
        })
        .catch(() => {});
}

function registerEvent(category, eventName) {
    const username = localStorage.getItem("username");
    if (!username) {
        alert("Please login first");
        return;
    }

    fetch("http://192.168.1.8:8080/api/registrations/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            category: category,
            eventName: eventName
        })
    })
    .then(res => {
        if (res.ok) {
            alert("Successfully registered!");
        } else {
            alert("Registration failed");
        }
    })
    .catch(() => {
        alert("Registration failed");
    });
}

function deleteEvent(eventId, cardElement) {
    if (!confirm("Delete this event?")) return;
    fetch(`http://192.168.1.8:8080/api/events/delete/${eventId}`, { method: "DELETE" })
        .then(res => {
            if (res.ok) { cardElement.remove(); alert("Event deleted"); }
            else alert("Failed to delete");
        })
        .catch(() => alert("Delete failed"));
}

function openEventDetails(category, eventName) {
    window.location.href = `event-details.html?category=${category}&event=${encodeURIComponent(eventName)}`;
}
