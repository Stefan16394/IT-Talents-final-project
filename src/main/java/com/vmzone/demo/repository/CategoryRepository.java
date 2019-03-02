package com.vmzone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
