package com.kaizenflow.bookquik.inventory.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaizenflow.bookquik.inventory.domain.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenueId(Long venueId);

    List<Event> findByNameContainingIgnoreCase(String name);

    List<Event> findByLeftCapacityGreaterThan(Long availableCapacity);

    @Query("SELECT e FROM Event e WHERE e.leftCapacity > 0 ORDER BY e.leftCapacity DESC")
    List<Event> findEventsWithAvailableCapacity();

    @Query("SELECT e FROM Event e JOIN FETCH e.venue WHERE e.id = :eventId")
    Event findEventWithVenue(Long eventId);
}
