package com.kaizenflow.bookquik.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaizenflow.bookquik.booking.domain.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    List<Order> findByEventId(Long eventId);

    List<Order> findByPlacedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.id = :orderId")
    Order findOrderWithCustomer(Long orderId);

    @Query("SELECT SUM(o.quantity) FROM Order o WHERE o.eventId = :eventId")
    Long getTotalTicketsBookedForEvent(Long eventId);
}
