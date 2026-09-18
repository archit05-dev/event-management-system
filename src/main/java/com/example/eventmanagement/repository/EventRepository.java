package com.example.eventmanagement.repository;

import com.example.eventmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByNameAndVenueAndStartDateTime(
            String name,
            String venue,
            LocalDateTime startDateTime
    );


}