package com.example.eventmanagement.controller;

import com.example.eventmanagement.dto.EventRequest;
import com.example.eventmanagement.entity.Event;
import com.example.eventmanagement.enums.EventStatus;
import com.example.eventmanagement.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }

    @GetMapping
    public Page<Event> getAllEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String venue,
            @RequestParam(required = false) EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "startDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return eventService.getAllEvents(
                search,
                venue,
                status,
                page,
                limit,
                sortBy,
                direction
        );
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {

        return eventService.updateEvent(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @PatchMapping("/{id}/cancel")
    public Event cancelEvent(@PathVariable Long id) {
        return eventService.cancelEvent(id);
    }
}