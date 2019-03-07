package com.vmzone.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
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

	public void addReview(AddReviewDTO review, long id) throws ResourceDoesntExistException {
		try {
			Review newReview = new Review(this.productRepository.findById(review.getProductId()).get(),
					this.userRepository.findById(id).get(), review.getReview(), review.getRating());
			this.reviewRepository.save(newReview);
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Invalid product or user");
		}
	}

	public void removeReviewById(long id, long userId) throws ResourceDoesntExistException {
		List<Review> reviews = this.reviewRepository.findReviewsByUser(userId);
		Review review = this.reviewRepository.findById(id);
		if (review == null || !reviews.contains(review)) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}
		review.setIsDeleted(1);
		this.reviewRepository.save(review);
	}

	public void editReview(long id, EditReviewDTO editedReview, long userId) throws ResourceDoesntExistException {
		List<Review> reviews = this.reviewRepository.findReviewsByUser(userId);
		Review review = this.reviewRepository.findById(id);
		if (review == null || !reviews.contains(review)) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}

		review.setReview(editedReview.getReview());
		review.setRating(editedReview.getRating());
		review.setIsDeleted(editedReview.getIsDeleted());

		this.reviewRepository.save(review);

	}

}
