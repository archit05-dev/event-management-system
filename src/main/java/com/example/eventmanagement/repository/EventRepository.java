package com.example.eventmanagement.repository;

import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenueAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
            String venue,
            LocalDateTime newEndDateTime,
            LocalDateTime newStartDateTime
    );

    List<Event> findByVenueAndStartDateTimeLessThanAndEndDateTimeGreaterThanAndIdNot(
            String venue,
            LocalDateTime newEndDateTime,
            LocalDateTime newStartDateTime,
            Long id
    );

    Page<Event> findByNameContainingIgnoreCaseAndVenueContainingIgnoreCaseAndStatus(
            String name,
            String venue,
            EventStatus status,
            Pageable pageable
    );

    Page<Event> findByNameContainingIgnoreCaseAndVenueContainingIgnoreCase(
            String name,
            String venue,
            Pageable pageable
    );
}