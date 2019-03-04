package com.vmzone.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.service.ReviewService;

@RestController
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/review")
	public void addReview(@RequestBody AddReviewDTO review) {
		this.reviewService.addReview(review);
	}
	
	@PutMapping("/review/remove/{id}")
	public void removeReview(@PathVariable long id) throws ResourceDoesntExistException {
		 this.reviewService.removeReviewById(id);
	}
	
	@PutMapping("/review/edit/{id}")
	public void editReview(@PathVariable long id,@RequestBody EditReviewDTO review) throws ResourceDoesntExistException {
			this.reviewService.editReview(id, review);
	}
	
	
}
