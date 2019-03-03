package com.vmzone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.dto.ListSubCategory;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.repository.CategoryRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;

@Service
public class ProductService {
	private static final int SMALL_QUANTITY_INDICATOR = 10;

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
	
	public List<ListProduct> getAllProductsWithSmallQuantity(){
		return this.productRepository.findAll().stream()
				.filter(product -> product.getProductId() != null && product.getQuantity() < SMALL_QUANTITY_INDICATOR)
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

	public void removeProductById(long id) throws ResourceDoesntExistException {
		Product product = this.productRepository.findById(id).get();
		if(product == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
		product.setIsDeleted(1);
		this.productRepository.save(product);
	}

	public Product getProductById(long id) {
		return this.productRepository.findById(id).get();
	}

	public void editProduct( long id ,EditProductDTO editedProduct) throws ResourceDoesntExistException {
		Product product = this.productRepository.findById(id).get();
		if(product == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
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
	
	
	//TODO possibly a thread
	public void calculateRating() throws ResourceDoesntExistException {
		List<ListProduct> products = getAllproducts();
		
		if(products.isEmpty()) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "There are no products");
		}
		
		for(ListProduct p : products) {
			List<ListReview> reviews = getReviewsForProduct(p.getId());
			p.fillReviews(reviews);
			Product prod = this.productRepository.findById(p.getId()).get();
			if(reviews.isEmpty()) {
				prod.setRating(Double.valueOf(0));
			} else {
				int sum = 0;
				for(ListReview r : reviews) {
					sum += r.getRating();
				}
				Double average = (double) (sum / reviews.size());
				prod.setRating(average);
			}
			this.productRepository.save(prod);
		}
		
	}
}
