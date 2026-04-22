//package com.eventclub.backend.Entity; // adjust if your package is different
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//import java.time.LocalDateTime; // For LocalDateTime
////package com.eventclub.backend.Entity;
//
//import jakarta.persistence.*;
//
//@Entity
//public class Attendance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//
//    // ✅ ADD THIS (VERY IMPORTANT)
//    @ManyToOne
//    @JoinColumn(name = "event_id")
//    private Event event;
//
//    // ✅ GETTERS & SETTERS
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public Event getEvent() {
//        return event;
//    }
//
//    public void setEvent(Event event) {
//        this.event = event;
//    }
//}
package com.eventclub.backend.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String eventName;     // ✅ event name

    private String eventToken;    // ✅ QR token

    private LocalDateTime markedAt; // ✅ timestamp

    // getters and setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventToken() { return eventToken; }
    public void setEventToken(String eventToken) { this.eventToken = eventToken; }

    public LocalDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(LocalDateTime markedAt) { this.markedAt = markedAt; }
}
//@Entity
//public class Attendance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//    private String eventToken;
//    private LocalDateTime markedAt;
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEventToken() {
//        return eventToken;
//    }
//
//    public void setEventToken(String eventToken) {
//        this.eventToken = eventToken;
//    }
//
//    public LocalDateTime getMarkedAt() {
//        return markedAt;
//    }
//
//    public void setMarkedAt(LocalDateTime markedAt) {
//        this.markedAt = markedAt;
//    }
//}
