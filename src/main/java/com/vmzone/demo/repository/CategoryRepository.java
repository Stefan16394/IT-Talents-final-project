package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query(value="WITH RECURSIVE category_path (id, title, parent_id, path) AS \r\n" + 
				"		 ( SELECT category_id, name, parent_id, name as path \r\n" + 
				"			    FROM categories\r\n" + 
				"			    WHERE category_id = :id \r\n" + 
				"		  UNION ALL\r\n" + 
				"			 SELECT c.category_id, c.name, c.parent_id, CONCAT(cp.path, ' > ', c.name)\r\n" + 
				"			    FROM category_path cp JOIN categories c\r\n" + 
				"			      ON c.parent_id = cp.id)\r\n" + 
				"		\r\n" + 
				"			SELECT cp.id,cp.path FROM category_path cp\r\n" + 
				"			LEFT JOIN categories c ON cp.id=c.parent_id \r\n" + 
				"			WHERE c.category_id IS NULL", nativeQuery=true)
	List<Object> getLeafCategories(@Param("id") long id);
	
	Category findByName(String name);
	
}
