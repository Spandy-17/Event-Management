package com.eventclub.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eventclub.backend.Entity.EventRegistration;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    List<EventRegistration> findByUsername(String username);

    @Transactional
    @Modifying
    void deleteByEventName(String eventName);  // ✅ add this line
}