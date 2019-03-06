package com.vmzone.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
		
	@Query(value = "SELECT * FROM products p WHERE p.category_id IN :ids", nativeQuery = true)
	List<Object> getProductsPresentInCategories(@Param("ids") List<Long> categoriesIds);
	


}
