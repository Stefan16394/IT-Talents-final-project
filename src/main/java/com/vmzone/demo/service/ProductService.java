package com.vmzone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.dto.ListSubCategory;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.repository.CategoryRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public void addProduct(AddProductDTO product ) {
		System.out.println(product);
		Product newProduct = new Product(
				this.categoryRepository.findById(product.getCategoryId()).get(),
				product.getTitle(),product.getInformation(),product.getInStock(),
				product.getDelivery(),
				product.getDate(),
				product.getQuantity(),
				product.getInSale(),product.getDetailedInformation()
				);
		this.productRepository.save(newProduct);
	}
	
	public List<ListReview> getReviewsForProduct(long id) {
		return this.reviewRepository.findAll().stream()
				.filter(review -> review.getReviewId() != null && review.getProduct().getProductId().equals(id))
				.map(review ->  new ListReview(review.getReviewId(), review.getReview(), review.getRating()))
				.collect(Collectors.toList());
	}
	
	public ListProduct getAllInfoForProduct(long id) throws BadCredentialsException {
		Product p = this.productRepository.findById(id).get();
		if(p == null) {
			throw new BadCredentialsException();
		}
		List<ListReview> reviews = getReviewsForProduct(id);
		
		ListProduct info = new ListProduct(p.getProductId(), p.getTitle(), p.getInformation(), p.getInStock(), p.getDelivery(), p.getDetailedInformation());
		info.fillReviews(reviews);
		
		return info;
		
	}
	public List<ListProduct> getAllproducts(){
		return this.productRepository.findAll().stream()
				.filter(product -> product.getProductId() != null)
				.map(product -> {
					try {
						return getAllInfoForProduct(product.getProductId());
					} catch (BadCredentialsException e) {
						e.printStackTrace();
					}
					return null;
				})
				.collect(Collectors.toList());
	}
}
