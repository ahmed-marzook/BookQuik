package com.kaizenflow.bookquik.inventory.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaizenflow.bookquik.inventory.domain.entity.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    Optional<Venue> findByName(String name);

    List<Venue> findByTotalCapacityGreaterThanEqual(Long capacity);

    @Query("SELECT v FROM Venue v WHERE LOWER(v.address) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Venue> findByAddressContainingIgnoreCase(String location);
}
