let html5QrCode;

function openScanner() {
    const role = localStorage.getItem("role");

    if (role !== "student") {
        alert("Only students can scan QR");
        return;
    }

    document.getElementById("scannerPanel").style.display = "flex";

    html5QrCode = new Html5Qrcode("reader");

    html5QrCode.start(
        { facingMode: "environment" },
        {
            fps: 10,
            qrbox: 250
        },
        (decodedText) => {
            console.log("Scanned:", decodedText);

            markAttendance(decodedText);

            html5QrCode.stop();
            closeScanner();
        },
        (error) => {
            // ignore scan errors
        }
    ).catch(err => {
        alert("Camera error: " + err);
    });
}

function closeScanner() {
    document.getElementById("scannerPanel").style.display = "none";

    if (html5QrCode) {
        html5QrCode.stop().catch(() => {});
    }
}

async function markAttendance(scannedToken) {
    try {
        const res = await fetch("http://192.168.1.8:8080/api/attendance/mark", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                token: scannedToken,
                username: localStorage.getItem("username")
            })
        });

        const msg = await res.text();
        alert(msg);

    } catch (err) {
        alert("Error marking attendance");
    }
}
