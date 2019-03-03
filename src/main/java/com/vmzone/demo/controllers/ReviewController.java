package com.vmzone.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.service.ReviewService;

@RestController
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/review")
	public void addReview(@RequestBody AddReviewDTO review) {
		this.reviewService.addReview(review);
	}
	
	
}
