package com.vmzone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vmzone.demo.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
