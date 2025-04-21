package com.kaizenflow.bookquik.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaizenflow.bookquik.inventory.domain.request.CreateEventRequest;
import com.kaizenflow.bookquik.inventory.domain.request.CreateVenueRequest;
import com.kaizenflow.bookquik.inventory.domain.response.EventInventoryResponse;
import com.kaizenflow.bookquik.inventory.domain.response.VenueInventoryResponse;
import com.kaizenflow.bookquik.inventory.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/events")
    public List<EventInventoryResponse> inventoryGetAllEvents() {
        return inventoryService.getAllEvents();
    }

    @GetMapping("/venues")
    public List<VenueInventoryResponse> inventoryGetAllVenues() {
        return inventoryService.getAllVenues();
    }

    @GetMapping("/venue/{venueId}")
    public VenueInventoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId) {
        return inventoryService.getVenueByInformation(venueId);
    }

    @GetMapping("/event/{eventId}")
    public EventInventoryResponse inventoryByEventId(@PathVariable("eventId") Long eventId) {
        return inventoryService.getEventInventory(eventId);
    }

    @PutMapping("/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(
            @PathVariable("eventId") Long eventId, @PathVariable("capacity") Long ticketsBooked) {
        inventoryService.updateEventCapacity(eventId, ticketsBooked);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/venue")
    public ResponseEntity<VenueInventoryResponse> createVenue(
            @Valid @RequestBody CreateVenueRequest request) {
        VenueInventoryResponse createdVenue = inventoryService.createVenue(request);
        return new ResponseEntity<>(createdVenue, HttpStatus.CREATED);
    }

    @PostMapping("/event")
    public ResponseEntity<EventInventoryResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request) {
        EventInventoryResponse createdEvent = inventoryService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}
