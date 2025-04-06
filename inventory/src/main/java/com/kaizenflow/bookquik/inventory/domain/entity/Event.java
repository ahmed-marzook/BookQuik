package com.kaizenflow.bookquik.inventory.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_capacity", nullable = false)
    private Long totalCapacity;

    @Column(name = "left_capacity", nullable = false)
    private Long leftCapacity;

    @Column(name = "ticket_price", nullable = false)
    private BigDecimal ticketPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}
