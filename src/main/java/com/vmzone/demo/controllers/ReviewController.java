package com.vmzone.demo.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Review;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.ReviewService;
import com.vmzone.demo.utils.SessionManager;

@RestController
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/review")
	public Review addReview(@RequestBody AddReviewDTO review, HttpSession session) throws ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		return this.reviewService.addReview(review, SessionManager.getLoggedUserId(session));
	}
	
	@PutMapping("/review/remove/{id}")
	public void removeReview(@PathVariable long id, HttpSession session) throws ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		 this.reviewService.removeReviewById(id, SessionManager.getLoggedUserId(session));
	}
	
	@PutMapping("/review/edit/{id}")
	public void editReview(@PathVariable long id,@RequestBody EditReviewDTO review, HttpSession session) throws ResourceDoesntExistException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}	
		this.reviewService.editReview(id, review, SessionManager.getLoggedUserId(session));
	}
}
