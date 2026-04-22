package com.eventclub.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eventclub.backend.Entity.Student;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>
{
	//Optional<Student> findByName(String name);
	 Student findByNameAndPassword(String name, String password);
}
