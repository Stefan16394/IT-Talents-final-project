package com.vmzone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vmzone.demo.models.OrderDetails;

public interface OrderDetailsRepository  extends JpaRepository<OrderDetails, Long>{

}
