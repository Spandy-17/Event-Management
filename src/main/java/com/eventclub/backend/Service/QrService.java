package com.eventclub.backend.Service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QrService {

    private final Map<Long, String> activeQrMap = new HashMap<>();
    private final Map<Long, Long> expiryMap = new HashMap<>();

    public String generateQr(Long eventId) {

        String token = eventId + "_" + UUID.randomUUID();
        long expiryTime = Instant.now().getEpochSecond() + 30;

        activeQrMap.put(eventId, token);
        expiryMap.put(eventId, expiryTime);

        return token;
    }

    public boolean validateQr(Long eventId, String token) {

        if (!activeQrMap.containsKey(eventId)) return false;

        long now = Instant.now().getEpochSecond();
        long expiry = expiryMap.get(eventId);

        if (now > expiry) return false;

        return activeQrMap.get(eventId).equals(token);
    }
}

