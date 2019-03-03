package com.vmzone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.models.Category;
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
				product.getQuantity(),
				product.getInSale(),
				product.getDetailedInformation()
				);
		this.productRepository.save(newProduct);
	}
	
	public List<ListReview> getReviewsForProduct(long id) {
		return this.reviewRepository.findAll().stream()
				.filter(review -> review.getReviewId() != null && review.getProduct().getProductId().equals(id))
				.map(review ->  new ListReview(review.getReviewId(), review.getReview(), review.getRating()))
				.collect(Collectors.toList());
	}
	
	public List<ListProduct> getAllproducts(){
		return this.productRepository.findAll().stream()
				.filter(product -> product.getProductId() != null)
				.map(product -> new ListProduct(product.getProductId(),
						product.getTitle(),
						product.getInformation()))
				.collect(Collectors.toList());
	}

	public void removeProductById(long id) {
		Product product = this.productRepository.findById(id).get();
		product.setIsDeleted(1);
		this.productRepository.save(product);
	}

	public Product getProductById(long id) {
		return this.productRepository.findById(id).get();
	}

	public void editProduct( long id ,EditProductDTO editedProduct) {
		Product product = this.productRepository.findById(id).get();
		
		// TODO validation on optional
		Category cat = this.categoryRepository.findById(editedProduct.getCategoryId()).get();
		product.setCategory(cat);
		product.setDelivery(editedProduct.getDelivery());
		product.setDetailedInformation(editedProduct.getDetailedInformation());
		product.setInformation(editedProduct.getInformation());
		product.setInSale(editedProduct.getInSale());
		product.setInStock(editedProduct.getInStock());
		product.setIsDeleted(editedProduct.getIsDeleted());
		product.setQuantity(editedProduct.getQuantity());
		product.setTitle(editedProduct.getTitle());
		
		this.productRepository.save(product);
		
	}
}
