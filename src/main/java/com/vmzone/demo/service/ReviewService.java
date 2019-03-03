package com.vmzone.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.models.Review;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;
import com.vmzone.demo.repository.UserRepository;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
	public void addReview(AddReviewDTO review ) {
		System.out.println(review);
		Review newReview = new Review(
				this.productRepository.findById(review.getProductId()).get(),
				this.userRepository.findById(review.getUserId()).get(),
				review.getReview(), 
				review.getRating());
		this.reviewRepository.save(newReview);
	}

}
