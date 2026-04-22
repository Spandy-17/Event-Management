package com.eventclub.backend.Repository;

import com.eventclub.backend.Entity.Attendance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByUsernameAndEventName(String username, String eventName);

    @Transactional
    @Modifying
    void deleteByEventName(String eventName);
}