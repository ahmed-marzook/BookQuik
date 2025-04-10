package com.kaizenflow.bookquik.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;
import com.kaizenflow.bookquik.inventory.domain.entity.Venue;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import com.kaizenflow.bookquik.inventory.domain.response.VenueInventoryResponse;
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
        final Venue venue = venueRepository.findById(venueId).orElse(null);

        return venueMapper.entityToResponse(venue);
    }

    public EventInventoryResponse getEventInventory(Long eventId) {
        return eventMapper.entityToResponse(
                eventRepository.findById(eventId).orElseThrow(RuntimeException::new));
    }

    public void updateEventCapacity(Long eventId, Long ticketsBooked) {
        final Event event = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);
        event.setLeftCapacity(event.getLeftCapacity() - ticketsBooked);
        eventRepository.save(event);
        log.info(
                "Updated event capacity for event id: {} with tickets booked: {}", eventId, ticketsBooked);
    }
}
