package com.vmzone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
}
