package com.eventclub.backend.Controller;

import org.springframework.http.ResponseEntity;
import com.eventclub.backend.Entity.Event;
import com.eventclub.backend.Repository.AttendanceRepository;
import com.eventclub.backend.Repository.EventRegistrationRepository;
import com.eventclub.backend.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/events")

public class EventController {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/create")
    public Event createEvent(
            @RequestParam("eventName") String eventName,
            @RequestParam("location") String location,
            @RequestParam("venue") String venue,
            @RequestParam("description") String description,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("createdBy") String createdBy,
            @RequestParam(value = "poster", required = false) MultipartFile poster) {

        Event event = new Event();

        event.setEventName(eventName);
        event.setLocation(location);
        event.setVenue(venue);
        event.setDescription(description);
        event.setStartTime(LocalDateTime.parse(startTime));
        event.setEndTime(LocalDateTime.parse(endTime));
        event.setCreatedBy(createdBy);

        try {
        	if (poster != null && !poster.isEmpty()) {
        	    String fileName = System.currentTimeMillis() + "_" + poster.getOriginalFilename();

        	    Path uploadPath = Paths.get("uploads");

        	    if (!Files.exists(uploadPath)) {
        	        Files.createDirectories(uploadPath);
        	    }

        	    Path filePath = uploadPath.resolve(fileName);
        	    poster.transferTo(filePath.toFile());

        	    event.setPoster(fileName);
        	}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventRepository.save(event);
    }

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/organized/{username}")
    public List<Event> getOrganizedEvents(@PathVariable String username) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getCreatedBy() != null &&
                        event.getCreatedBy().trim().equalsIgnoreCase(username.trim()))
                .toList();
    }

    @DeleteMapping("/delete/{eventName}")
    public ResponseEntity<String> deleteEvent(@PathVariable String eventName) {
        try {
            System.out.println("Deleting event: " + eventName);

            attendanceRepository.deleteByEventName(eventName);
            eventRegistrationRepository.deleteByEventName(eventName);
            eventRepository.deleteByEventName(eventName);

            return ResponseEntity.ok("Deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Delete failed: " + e.getMessage());
        }
    }

    @GetMapping("/poster/{fileName}")
    public ResponseEntity<byte[]> getPoster(@PathVariable String fileName) {
        try {
            Path path = Paths.get("uploads").resolve(fileName);
            byte[] image = Files.readAllBytes(path);

            String contentType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(image);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}