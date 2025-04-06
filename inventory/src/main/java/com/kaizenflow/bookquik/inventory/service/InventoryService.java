package com.kaizenflow.bookquik.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.inventory.domain.entity.Venue;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import com.kaizenflow.bookquik.inventory.domain.response.VenueInventoryResponse;
import com.kaizenflow.bookquik.inventory.mapper.EventMapper;
import com.kaizenflow.bookquik.inventory.mapper.VenueMapper;
import com.kaizenflow.bookquik.inventory.repositories.EventRepository;
import com.kaizenflow.bookquik.inventory.repositories.VenueRepository;

@Service
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
}
