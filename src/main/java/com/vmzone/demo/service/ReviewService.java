package com.vmzone.demo.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.Review;
import com.vmzone.demo.models.User;
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

	public Review addReview(AddReviewDTO review, long id) throws ResourceDoesntExistException {
		try {
			Review newReview = new Review(this.productRepository.findById(review.getProductId()).get(),
					this.userRepository.findById(id).get(), review.getReview(), review.getRating());
			Review rev = this.reviewRepository.save(newReview);
			calculateRating(review.getProductId());
			return rev;
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Invalid product or user");
		}
	}

	public void removeReviewById(long id, long userId) throws ResourceDoesntExistException {
		Review review = this.reviewRepository.findReviewForUser(userId, id);
		if (review == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}
		review.setIsDeleted(1);
		this.reviewRepository.save(review);
	}

	public Review editReview(long id, EditReviewDTO editedReview, long userId) throws ResourceDoesntExistException {
		Review review = this.reviewRepository.findReviewForUser(userId, id);
		if (review == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}

		review.setReview(editedReview.getReview());
		review.setRating(editedReview.getRating());
		review.setIsDeleted(editedReview.getIsDeleted());

		return this.reviewRepository.save(review);
	}
	
	public Review getReviewById(long id,long userId) throws ResourceDoesntExistException {
		Review review = this.reviewRepository.findReviewForUser(userId, id);
		if (review == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Review doesn't exist or it is not your review");
		}
		return review;
	}
	
	private List<ListReview> getReviewsForProduct(long id) {
		return this.reviewRepository.findReviewsForProduct(id).stream()
				.map(review -> new ListReview(review.getReviewId(), review.getReview(), review.getRating()))
				.collect(Collectors.toList());
	}
	
	private void calculateRating(long id) throws ResourceDoesntExistException {
		Product p = null;	
		try {
				p = this.productRepository.findById(id).get();
			} catch (NoSuchElementException e) {
				throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Invalid product or user");
			}
		
			List<ListReview> reviews = getReviewsForProduct(p.getProductId());
			if (reviews.isEmpty()) {
				p.setRating(Double.valueOf(0));
			} else {
				int sum = 0;
				for (ListReview r : reviews) {
					sum += r.getRating();
				}
				Double average = (double) (sum / reviews.size());
				p.setRating(average);
			}
			this.productRepository.save(p);
		}


}
