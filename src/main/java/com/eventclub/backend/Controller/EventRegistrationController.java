package com.eventclub.backend.Controller;

import com.eventclub.backend.Entity.EventRegistration;
import com.eventclub.backend.Repository.EventRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin
public class EventRegistrationController {

    @Autowired
    private EventRegistrationRepository registrationRepository;
    @PostMapping("/register")
    public ResponseEntity<?> registerEvent(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String category = data.get("category");
        String eventName = data.get("eventName");

        EventRegistration registration = new EventRegistration();
        registration.setUsername(username);
        registration.setCategory(category);
        registration.setEventName(eventName);

        registrationRepository.save(registration);

        return ResponseEntity.ok("Registration successful");
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserRegistrations(@PathVariable String username) {
        return ResponseEntity.ok(
            registrationRepository.findByUsername(username)
        );
    }
    

//    @PostMapping("/register")
//    public ResponseEntity<?> registerEvent(@RequestBody Map<String, String> data) {
//
//        Long eventId = Long.parseLong(data.get("eventId"));
//        String username = data.get("username");
//
//        // Check if already registered
//        EventRegistration existing =
//                registrationRepository.findByEventIdAndUsername(eventId, username);
//
//        if (existing != null) {
//            return ResponseEntity.badRequest().body("Already registered");
//        }
//
//        EventRegistration registration = new EventRegistration();
//        registration.setEventId(eventId);
//        registration.setUsername(username);
//
//        registrationRepository.save(registration);
//
//        return ResponseEntity.ok("Registration successful");
//    }
    // changes 
    
}
