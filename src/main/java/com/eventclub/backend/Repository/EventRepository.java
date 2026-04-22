package com.eventclub.backend.Repository;

import com.eventclub.backend.Entity.Event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // We can add custom queries later if needed
	List<Event> findByCreatedBy(String createdBy);
	
	@Transactional
	@Modifying
	void deleteByEventName(String eventName);
}
