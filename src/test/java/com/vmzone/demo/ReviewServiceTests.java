package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.vmzone.demo.dto.AddReviewDTO;
import com.vmzone.demo.dto.EditReviewDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.Review;
import com.vmzone.demo.models.User;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;
import com.vmzone.demo.repository.UserRepository;
import com.vmzone.demo.service.ReviewService;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ReviewServiceTests {

	private static final long DEFAULT_ID_TO_SEARCH = 1;

	private static final EditReviewDTO TEST_EDIT_REVIEW_OBJECT = new EditReviewDTO("Edited", 3, 1);

	private static final Review TEST_REVIEW_OBJECT = new Review(new Product(), new User(), "Great review", 5);

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	ReviewService reviewService;

	@Test(expected = ResourceDoesntExistException.class)
	public void testEditReviewByIdByWhenReviewIsNotFound() throws ResourceDoesntExistException {
		when(reviewRepository.findReviewForUser(DEFAULT_ID_TO_SEARCH,DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		EditReviewDTO e = TEST_EDIT_REVIEW_OBJECT;
		reviewService.editReview(DEFAULT_ID_TO_SEARCH, e,DEFAULT_ID_TO_SEARCH);
	}

	@Test
	public void testEditReviewByIdWhenReviewExists() throws ResourceDoesntExistException {
		when(reviewRepository.findReviewForUser(DEFAULT_ID_TO_SEARCH,DEFAULT_ID_TO_SEARCH)).thenReturn(TEST_REVIEW_OBJECT);
		Review review = TEST_REVIEW_OBJECT;
		EditReviewDTO editBody = TEST_EDIT_REVIEW_OBJECT;
		reviewService.editReview(DEFAULT_ID_TO_SEARCH, editBody,DEFAULT_ID_TO_SEARCH);
		assertEquals(editBody.getReview(), review.getReview());
		assertEquals(editBody.getRating(), review.getRating());
		assertEquals(editBody.getIsDeleted(), review.getIsDeleted());

	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testRemoveReviewByIdWhenReviewIsNotFound() throws ResourceDoesntExistException {
		when(reviewRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		reviewService.removeReviewById(DEFAULT_ID_TO_SEARCH,DEFAULT_ID_TO_SEARCH);
	}

	@Test
	public void testRemoveReviewByIdWhenReviewExists() throws ResourceDoesntExistException {
		final int EXPECTED_VALUE = 1;
		when(reviewRepository.findReviewForUser(DEFAULT_ID_TO_SEARCH,DEFAULT_ID_TO_SEARCH)).thenReturn(TEST_REVIEW_OBJECT);
		reviewService.removeReviewById(DEFAULT_ID_TO_SEARCH,DEFAULT_ID_TO_SEARCH);
		assertEquals(EXPECTED_VALUE, TEST_REVIEW_OBJECT.getIsDeleted());
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testAddReviewWithInvalidProductId() throws ResourceDoesntExistException {
		Optional<User> user = Optional.of(new User());
		when(userRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(user);
		when(productRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		reviewService.addReview(new AddReviewDTO(),DEFAULT_ID_TO_SEARCH);
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testAddReviewWithInvalidUserId() throws ResourceDoesntExistException {
		when(userRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(null);
		Optional<Product> product = Optional.of(new Product());
		when(productRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(product);
		reviewService.addReview(new AddReviewDTO(),DEFAULT_ID_TO_SEARCH);
	}

	@Test
	public void testAddReviewWithCorrectInput() throws ResourceDoesntExistException {
		Optional<User> user = Optional.of(new User());
		Optional<Product> product = Optional.of(new Product());
		when(userRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(user);
		when(productRepository.findById(DEFAULT_ID_TO_SEARCH)).thenReturn(product);
		reviewService.addReview(new AddReviewDTO(DEFAULT_ID_TO_SEARCH, "Review", 4),DEFAULT_ID_TO_SEARCH);
	}

	@Configuration
	static class Config {

	}
}
