package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
		
	@Query(value = "SELECT * FROM products p WHERE p.category_id IN :ids", nativeQuery = true)
	List<Object> getProductsPresentInCategories(@Param("ids") List<Long> categoriesIds);
	
	@Query(value = "SELECT * FROM products p WHERE p.price BETWEEN :min AND :max", nativeQuery = true)
	public List<Product> searchPrice(@Param("min") double min, @Param("max") double max);
	
	@Query(value = "SELECT * FROM products p WHERE p.title LIKE :str OR p.information LIKE :str", nativeQuery = true)
	public List<Product> search(@Param("str") String str);
	
	@Query(value = "SELECT * FROM products p WHERE p.quantity < :quantity",nativeQuery=true)
	public List<Product> getAllProductsWithSmallQuantity(@Param("quantity") int quantity);
	


}
