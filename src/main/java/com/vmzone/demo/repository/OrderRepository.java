package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.vmzone.demo.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "SELECT * FROM orders WHERE user_id = :id", nativeQuery = true)
	List<Order> findOrdersByUserId(@Param("id") long id);

	@Query(value = "SELECT * FROM orders WHERE order_id = :orderId AND user_id = :userId",nativeQuery=true)
	Order findOrderByUserId(@Param("orderId") long id, @Param("userId") long userId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM shopping_cart WHERE user_id = :id",nativeQuery=true)
	public void clearShoppingCart(@Param("id") long id);
}
