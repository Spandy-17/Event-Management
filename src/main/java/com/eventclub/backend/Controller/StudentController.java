//package com.eventclub.backend.Controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.eventclub.backend.Entity.Student;
//import com.eventclub.backend.Service.StudentService;
//
//@RestController
//@RequestMapping("/api/students")
//@CrossOrigin
//public class StudentController {
//
//    @Autowired
//    private StudentService studentService;
//
//    @PostMapping("/signup")
//    public Student registerStudent(@RequestBody Student student) {
//        return studentService.registerStudent(student);
//    }
//    @PostMapping("/login")
//    public Student loginStudent(@RequestBody Student student) {
//        return studentService.loginStudent(student.getName());
//    }
//}
//
package com.eventclub.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventclub.backend.Entity.Student;
import com.eventclub.backend.Service.StudentService;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ---------------- SIGNUP ----------------
    @PostMapping("/signup")
    public Student registerStudent(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String, String> data) {

        String username = data.get("name");
        String password = data.get("password");

        Student student = studentService.loginStudent(username, password);

        if (student == null) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        return ResponseEntity.ok(student);
    }
}
