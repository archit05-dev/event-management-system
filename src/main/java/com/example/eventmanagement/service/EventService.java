package com.example.eventmanagement.service;

import com.example.eventmanagement.dto.EventRequest;
import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.enums.EventStatus;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
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

}