package com.kaizenflow.bookquik.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;
import com.kaizenflow.bookquik.inventory.domain.entity.Venue;
import com.kaizenflow.bookquik.inventory.domain.request.CreateEventRequest;
import com.kaizenflow.bookquik.inventory.domain.request.CreateVenueRequest;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import com.kaizenflow.bookquik.inventory.domain.response.VenueInventoryResponse;
import com.kaizenflow.bookquik.inventory.exception.ResourceNotFoundException;
import com.kaizenflow.bookquik.inventory.mapper.EventMapper;
import com.kaizenflow.bookquik.inventory.mapper.VenueMapper;
import com.kaizenflow.bookquik.inventory.repositories.EventRepository;
import com.kaizenflow.bookquik.inventory.repositories.VenueRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;
    private final VenueMapper venueMapper;

    public InventoryService(
            EventRepository eventRepository,
            VenueRepository venueRepository,
            EventMapper eventMapper,
            VenueMapper venueMapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventMapper = eventMapper;
        this.venueMapper = venueMapper;
    }

    public List<EventInventoryResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public VenueInventoryResponse getVenueByInformation(Long venueId) {
        final Venue venue =
                venueRepository
                        .findById(venueId)
                        .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", venueId));

        return venueMapper.entityToResponse(venue);
    }

    public EventInventoryResponse getEventInventory(Long eventId) {
        return eventMapper.entityToResponse(
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId)));
    }

    @Transactional
    public void updateEventCapacity(Long eventId, Long ticketsBooked) {
        final Event event =
                eventRepository
                        .findById(eventId)
                        .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getLeftCapacity() < ticketsBooked) {
            throw new IllegalArgumentException("Not enough capacity available for event: " + eventId);
        }

        event.setLeftCapacity(event.getLeftCapacity() - ticketsBooked);
        eventRepository.save(event);
        log.info(
                "Updated event capacity for event id: {} with tickets booked: {}", eventId, ticketsBooked);
    }

    @Transactional
    public VenueInventoryResponse createVenue(CreateVenueRequest request) {
        log.info("Creating new venue: {}", request.name());

        Venue venue = new Venue();
        venue.setName(request.name());
        venue.setTotalCapacity(request.totalCapacity());
        venue.setAddress(request.address());

        Venue savedVenue = venueRepository.save(venue);
        log.info("Created new venue with id: {}", savedVenue.getId());

        return venueMapper.entityToResponse(savedVenue);
    }

    @Transactional
    public EventInventoryResponse createEvent(CreateEventRequest request) {
        log.info("Creating new event: {} at venue id: {}", request.name(), request.venueId());

        Venue venue =
                venueRepository
                        .findById(request.venueId())
                        .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", request.venueId()));

        Event event = new Event();
        event.setName(request.name());
        event.setTotalCapacity(request.totalCapacity());
        event.setLeftCapacity(request.totalCapacity()); // Initially, all capacity is available
        event.setTicketPrice(request.ticketPrice());
        event.setVenue(venue);

        Event savedEvent = eventRepository.save(event);
        log.info("Created new event with id: {}", savedEvent.getId());

        return eventMapper.entityToResponse(savedEvent);
    }

    public List<VenueInventoryResponse> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(venueMapper::entityToResponse)
                .collect(Collectors.toList());
    }
}
