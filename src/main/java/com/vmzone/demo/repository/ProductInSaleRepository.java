package com.vmzone.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;

public interface ProductInSaleRepository extends JpaRepository<ProductInSale, Long> {
	
	
	 List<ProductInSale> findById(long id);
	
	 ProductInSale findFirstByStartDate(LocalDateTime dateTime);
	 ProductInSale findFirstByEndDate(LocalDateTime dateTime);
	 ProductInSale findFirstByProducts(Product products);

}
