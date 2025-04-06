package com.kaizenflow.bookquik.inventory.service;

import java.util.List;
import java.util.stream.Collectors;

import com.kaizenflow.bookquik.inventory.mapper.EventMapper;
import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import com.kaizenflow.bookquik.inventory.repositories.EventRepository;
import com.kaizenflow.bookquik.inventory.repositories.VenueRepository;

@Service
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;

    public InventoryService(EventRepository eventRepository, VenueRepository venueRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventMapper = eventMapper;
    }

    public List<EventInventoryResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(eventMapper::eventInventoryResponse).collect(Collectors.toList());
    }
}
