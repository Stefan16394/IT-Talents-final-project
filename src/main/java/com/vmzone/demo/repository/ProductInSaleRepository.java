package com.vmzone.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;

public interface ProductInSaleRepository extends JpaRepository<ProductInSale, Long> {
	
	
	 List<ProductInSale> findById(long id);
	
	 
	 @Query(value = "select * from in_sale where product_id = :prodId and start_date = :startDate and end_date = :endDate and discount_percentage = :percentage", nativeQuery = true)
	 ProductInSale getProduct(@Param("prodId") long prodId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("percentage") int percentage );

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM in_sale WHERE end_date < :date", nativeQuery = true)
	void deleteExpired(@Param("date") LocalDateTime date);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM in_sale WHERE sale_id = :id ", nativeQuery = true)
	void deletePromotion(@Param("id") long id);

}
