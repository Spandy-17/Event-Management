//package com.eventclub.backend.Controller;  // change according to your package
//
package com.eventclub.backend.Controller;

import com.eventclub.backend.Entity.Attendance;
import com.eventclub.backend.Repository.AttendanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/mark")
    public String markAttendance(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String token = data.get("token");

        if (username == null || token == null) {
            return "<h2>❌ Missing data</h2>";
        }

        // ✅ SAFE TOKEN HANDLING
        String eventName = token;

        if (token.contains("-")) {
            String[] parts = token.split("-");
            eventName = parts[0];
        }

        System.out.println("USERNAME: " + username);
        System.out.println("TOKEN: " + token);

        // ✅ SAVE DATA
        Attendance attendance = new Attendance();
        attendance.setUsername(username);
        attendance.setEventName(eventName);
        attendance.setEventToken(token);
        attendance.setMarkedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return "<h2>✅ Attendance marked successfully!</h2>";
    }
    @GetMapping("/check")
    public boolean checkAttendance(
            @RequestParam String username,
            @RequestParam String eventName) {

        return attendanceRepository.existsByUsernameAndEventName(username, eventName);
    }
}
//import com.eventclub.backend.Entity.Attendance;
//import com.eventclub.backend.Repository.AttendanceRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/attendance")
//@CrossOrigin
//public class AttendanceController {
//
//    @Autowired
//    private AttendanceRepository repo;
//    @GetMapping("/mark")
//    public String markAttendance(@RequestParam Long eventId) {
//
//        System.out.println("Attendance marked for event: " + eventId);
//
//        return "<h2>✅ Attendance marked successfully!</h2>";
//    }
//    @PostMapping("/mark")
//    public String markAttendance(@RequestBody Map<String, String> data) {
//
//        String username = data.get("username");
//        Long eventId = Long.parseLong(data.get("eventId"));
//
//        System.out.println(username + " attended event " + eventId);
//
//        return "✅ Attendance marked successfully!";
//    }
//    @PostMapping("/mark")
//    public String markAttendance(@RequestBody Map<String, String> data) {
//
//        String username = data.get("username");
//        String eventIdStr = data.get("eventId");
//
//        if (username == null || eventIdStr == null) {
//            return "<h2>❌ Missing data</h2>";
//        }
//
//        Long eventId = Long.parseLong(eventIdStr);
//
//        // 👉 DEBUG PRINT (VERY IMPORTANT)
//        System.out.println("USERNAME: " + username);
//        System.out.println("EVENT ID: " + eventId);
//
//        // 👉 FETCH EVENT FROM DB
//        Event event = eventRepository.findById(eventId).orElse(null);
//
//        if (event == null) {
//            return "<h2>❌ Event not found</h2>";
//        }
//
//        // 👉 CREATE ATTENDANCE ENTRY
//        Attendance attendance = new Attendance();
//        attendance.setUsername(username);
//        attendance.setEvent(event);
//
//        attendanceRepository.save(attendance);
//
//        return "<h2>✅ Attendance marked successfully!</h2>";
//    }
    //@PostMapping("/mark")
//    public ResponseEntity<?> markAttendance(@RequestBody Map<String, String> data) {
//
//        String token = data.get("token");
//        String username = data.get("username");
//
//        if (repo.existsByUsernameAndEventToken(username, token)) {
//            return ResponseEntity.badRequest().body("Already marked");
//        }
//
//        Attendance a = new Attendance();
//        a.setUsername(username);
//        a.setEventToken(token);
//        a.setMarkedAt(LocalDateTime.now());
//
//        repo.save(a);
//        return ResponseEntity.ok("Attendance marked");
//    }
    
//}
//package com.eventclub.backend.Controller;
//
//import com.eventclub.backend.Entity.Attendance;
//import com.eventclub.backend.Entity.Event;
//import com.eventclub.backend.Repository.AttendanceRepository;
//import com.eventclub.backend.Repository.EventRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/attendance")
//@CrossOrigin
//public class AttendanceController {
//
//    @Autowired
//    private AttendanceRepository attendanceRepository;
//
//    @Autowired
//    private EventRepository eventRepository;  // ✅ ADD THIS
//import java.time.LocalDateTime; // ✅ MUST ADD
//
//@PostMapping("/mark")
//public String markAttendance(@RequestBody Map<String, String> data) {
//
//    String username = data.get("username");
//    String token = data.get("token");
//
//    if (username == null || token == null) {
//        return "<h2>❌ Missing data</h2>";
//    }
//
//    String[] parts = token.split("-");
//    String eventName = parts[0];
//
//    System.out.println("USERNAME: " + username);
//    System.out.println("TOKEN: " + token);
//
//    Attendance attendance = new Attendance();
//    attendance.setUsername(username);
//    attendance.setEventName(eventName);
//    attendance.setEventToken(token);
//    attendance.setMarkedAt(LocalDateTime.now());
//
//    attendanceRepository.save(attendance);
//
//    return "<h2>✅ Attendance marked successfully!</h2>";
//}
//    @PostMapping("/mark")
//    public String markAttendance(@RequestBody Map<String, String> data) {
//
//        String username = data.get("username");
//        String eventIdStr = data.get("eventId");
//
//        if (username == null || eventIdStr == null) {
//            return "<h2>❌ Missing data</h2>";
//        }
//
//        Long eventId = Long.parseLong(eventIdStr);
//
//        System.out.println("USERNAME: " + username);
//        System.out.println("EVENT ID: " + eventId);
//
//        // ✅ FETCH EVENT
//        Event event = eventRepository.findById(eventId).orElse(null);
//
//        if (event == null) {
//            return "<h2>❌ Event not found</h2>";
//        }
//
//        // ✅ SAVE ATTENDANCE
//        Attendance attendance = new Attendance();
//        attendance.setUsername(username);
//        attendance.setEvent(event);
//
//        attendanceRepository.save(attendance);
//
//        return "<h2>✅ Attendance marked successfully!</h2>";
//    }
//    @PostMapping("/mark")
//    public String markAttendance(@RequestBody Map<String, String> data) {
//
//        String username = data.get("username");
//        String eventIdStr = data.get("eventId");
//
//        if (username == null || eventIdStr == null) {
//            return "<h2>❌ Missing data</h2>";
//        }
//
//        Long eventId = Long.parseLong(eventIdStr);
//
//        Event event = eventRepository.findById(eventId).orElse(null);
//
//        if (event == null) {
//            return "<h2>❌ Event not found</h2>";
//        }
//
//        // ✅ PREVENT DUPLICATE ENTRY
//        if (attendanceRepository.existsByUsernameAndEvent(username, event)) {
//            return "<h2>⚠️ Already marked</h2>";
//        }
//
//        Attendance attendance = new Attendance();
//        attendance.setUsername(username);
//        attendance.setEvent(event);
//
//        attendanceRepository.save(attendance);
//
//        return "<h2>✅ Attendance marked successfully!</h2>";
//    }
//    @PostMapping("/mark")
//    public String markAttendance(@RequestBody Map<String, String> data) {
//
//        String username = data.get("username");
//        String token = data.get("token"); // QR gives this
//
//        if (username == null || token == null) {
//            return "<h2>❌ Missing data</h2>";
//        }
//
//        // 👉 token format: eventName-eventId (you can customize)
//        String eventName = token.split("-")[0];
//
//        System.out.println("USERNAME: " + username);
//        System.out.println("TOKEN: " + token);
//
//        // 👉 Save attendance
//        Attendance attendance = new Attendance();
//        attendance.setUsername(username);
//        attendance.setEventName(eventName);
//        attendance.setEventToken(token);
//        attendance.setMarkedAt(LocalDateTime.now());
//
//        attendanceRepository.save(attendance);
//
//        return "<h2>✅ Attendance marked successfully!</h2>";
//    }
//}
