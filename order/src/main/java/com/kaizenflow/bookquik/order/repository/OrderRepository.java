package com.kaizenflow.bookquik.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaizenflow.bookquik.order.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
