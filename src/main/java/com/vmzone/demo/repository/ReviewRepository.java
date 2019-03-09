package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Favourite;
import com.vmzone.demo.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Review findById(long id);
	
	@Query(value = "select * from reviews where user_id = :id and review_id = :revId and is_deleted = 0", nativeQuery = true)
	Review findReviewForUser(@Param("id") long id, @Param("revId") long revId);
	
	@Query(value="SELECT * FROM reviews WHERE product_id = :id and is_deleted = 0",nativeQuery=true)
	List<Review> findReviewsForProduct(@Param("id") long id);
}
