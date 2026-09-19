package com.example.eventmanagement.service;

import com.example.eventmanagement.dto.EventRequest;
import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.enums.EventStatus;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(EventRequest request) {

        if (!request.getEndDateTime().isAfter(request.getStartDateTime())) {
            throw new IllegalArgumentException(
                    "End date and time must be after start date and time"
            );
        }

        List<Event> overlappingEvents =
                eventRepository
                        .findByVenueAndStartDateTimeLessThanAndEndDateTimeGreaterThan(
                                request.getVenue(),
                                request.getEndDateTime(),
                                request.getStartDateTime()
                        );

        if (hasOverlappingActiveEvent(overlappingEvents)) {
            throw new IllegalStateException(
                    "Another active event is already scheduled at this venue during the selected time"
            );
        }


        Event event = new Event();

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setVenue(request.getVenue());
        event.setMaxCapacity(request.getMaxCapacity());


        event.setStatus(EventStatus.UPCOMING);

        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }


    public Page<Event> getAllEvents(
            String search,
            String venue,
            EventStatus status,
            int page,
            int limit,
            String sortBy,
            String direction
    ) {

        if (page < 0) {
            throw new IllegalArgumentException("Page must be zero or greater");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be greater than zero");
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Direction must be either asc or desc"
            );
        }

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        String searchValue = search == null ? "" : search;
        String venueValue = venue == null ? "" : venue;

        if (status != null) {
            return eventRepository
                    .findByNameContainingIgnoreCaseAndVenueContainingIgnoreCaseAndStatus(
                            searchValue,
                            venueValue,
                            status,
                            pageable
                    );
        }

        return eventRepository
                .findByNameContainingIgnoreCaseAndVenueContainingIgnoreCase(
                        searchValue,
                        venueValue,
                        pageable
                );
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    public Event updateEvent(Long id, EventRequest request) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));


        if (event.getStatus() != EventStatus.UPCOMING) {
            throw new IllegalStateException(
                    "Only upcoming events can be updated"
            );
        }

        if (!request.getEndDateTime().isAfter(request.getStartDateTime())) {
            throw new IllegalArgumentException(
                    "End date and time must be after start date and time"
            );
        }

        List<Event> overlappingEvents =
                eventRepository
                        .findByVenueAndStartDateTimeLessThanAndEndDateTimeGreaterThanAndIdNot(
                                request.getVenue(),
                                request.getEndDateTime(),
                                request.getStartDateTime(),
                                id
                        );

        if (hasOverlappingActiveEvent(overlappingEvents)) {
            throw new IllegalStateException(
                    "Another active event is already scheduled at this venue during the selected time"
            );
        }


        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setVenue(request.getVenue());
        event.setMaxCapacity(request.getMaxCapacity());
        event.setUpdatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));


        if (event.getStatus() == EventStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed events cannot be deleted"
            );
        }

        eventRepository.delete(event);
    }

    public Event cancelEvent(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));


        if (event.getStatus() == EventStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed events cannot be cancelled"
            );
        }


        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Event is already cancelled"
            );
        }

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(LocalDateTime.now());

        return eventRepository.save(event);
    }

    private boolean hasOverlappingActiveEvent(List<Event> events) {
        return events.stream()
                .anyMatch(event ->
                        event.getStatus() == EventStatus.UPCOMING
                                || event.getStatus() == EventStatus.ONGOING
                );
    }

}