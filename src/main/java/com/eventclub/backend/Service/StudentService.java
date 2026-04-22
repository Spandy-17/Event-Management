package com.eventclub.backend.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eventclub.backend.Entity.Student;
import com.eventclub.backend.Repository.StudentRepository;
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student registerStudent(Student student) {
        return studentRepository.save(student);
     
    }
    public Student loginStudent(String username, String password) {
        return studentRepository.findByNameAndPassword(username, password);
    }
}

