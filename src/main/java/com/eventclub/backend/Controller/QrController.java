package com.eventclub.backend.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.eventclub.backend.Entity.Event;
import com.eventclub.backend.Repository.EventRepository;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = "*")
public class QrController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/generate/{eventId}")
    public ResponseEntity<byte[]> generateQR(@PathVariable Long eventId) throws Exception {

        // ✅ Fetch event from DB
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // ✅ Get event name
        String eventName = event.getEventName();

        // ✅ Create token
        String token = eventName + "-" + eventId;

        // ✅ QR URL (VERY IMPORTANT)
        String url = "http://192.168.1.8:8080/markAttendance.html?token=" + token;

        // ✅ Generate QR
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(pngOutputStream.toByteArray());
    }
}
