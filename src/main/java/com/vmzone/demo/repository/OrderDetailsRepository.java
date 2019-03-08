package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vmzone.demo.models.OrderDetails;

public interface OrderDetailsRepository  extends JpaRepository<OrderDetails, Long>{
	@Query(value = "SELECT * FROM order_details WHERE order_id = :id",nativeQuery = true)
	List<OrderDetails> getOrderDetailsForOrder(@Param("id") long id);
}
