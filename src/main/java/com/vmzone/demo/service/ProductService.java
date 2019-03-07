package com.vmzone.demo.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;

@Service
public class ProductService {
	private static final int SMALL_QUANTITY_INDICATOR = 10;


	@Autowired ProductRepository productRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ReviewRepository reviewRepository;

	public void addProduct(AddProductDTO product) {
		Product newProduct = new Product(
				this.categoryService.categoryRepository.findById(product.getCategoryId()).get(), product.getTitle(),
				product.getInformation(), product.getInStock(), product.getDelivery(), product.getQuantity(),
				product.getInSale(), product.getDetailedInformation());
		this.productRepository.save(newProduct);
	}

	public List<ListReview> getReviewsForProduct(long id) {
		return this.reviewRepository.findAll().stream()
				.filter(review -> review.getReviewId() != null && review.getProduct().getProductId().equals(id))
				.map(review -> new ListReview(review.getReviewId(), review.getReview(), review.getRating()))
				.collect(Collectors.toList());
	}

	public ListProduct getAllInfoForProduct(long id) throws BadCredentialsException {
		Product p = this.productRepository.findById(id).get();
		if (p == null) {
			throw new BadCredentialsException();
		}
		List<ListReview> reviews = getReviewsForProduct(id);

		ListProduct info = new ListProduct(p.getProductId(), p.getTitle(), p.getInformation(), p.getInStock(),
				p.getDelivery(), p.getDetailedInformation());
		info.fillReviews(reviews);

		return info;

	}

	public List<ListProductBasicInfo> getAllproducts(long id) {
		List<Long> ids = this.categoryService.getLeafCategories(id).stream().map(c -> c.getId())
				.collect(Collectors.toList());
		return this.getProductPresentInCategories(ids);
	}

	public List<ListProductBasicInfo> getProductPresentInCategories(List<Long> categoriesIds) {
		
		List resultSet = this.productRepository.getProductsPresentInCategories(categoriesIds);
		List<ListProductBasicInfo> result = new ArrayList<>();
		for (Object o : resultSet) {
			Object[] row = (Object[]) o;
			ListProductBasicInfo p = new ListProductBasicInfo(Long.parseLong(row[0].toString()), row[2].toString());
			result.add(p);
		}
		return result;
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
		Category cat = this.categoryService.categoryRepository.findById(editedProduct.getCategoryId()).get();
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
	//TODO do for colour and size
	public List<ListProductBasicInfo> getAllproducts(String sortBy, Long categoryId){
		return this.productRepository.findAll().stream()
				.filter(product -> categoryId == null || product.getCategory().getCategoryId().equals(categoryId))
				.sorted((p1, p2) -> {
					switch(sortBy) {
					case "newest" : return p1.getDate().compareTo(p2.getDate());
					case "oldest" : return p2.getDate().compareTo(p1.getDate());
					case "ascending price" : return Double.compare(p1.getPrice(), p2.getPrice());
					case "descending price" : return Double.compare(p2.getPrice(), p1.getPrice());
					case "ascending alphabetic" : return p1.getTitle().compareTo(p2.getTitle());
					case "descending alphabetic" : return p2.getTitle().compareTo(p1.getTitle());
					case "fastest delivery" : return p1.getDelivery() - p2.getDelivery();
					case "ascending rating" : return Double.compare(p1.getRating(), p2.getRating());
					case "descending rating" : return Double.compare(p2.getRating(), p1.getRating());
					default : return 1;
					}
				})
				.map(product -> new ListProductBasicInfo(product.getProductId(), product.getTitle(), product.getPrice(), product.getDate()))
				.collect(Collectors.toList());
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
	
	public List<ListProductBasicInfo> searchPrice(double min, double max){
	
		List<Product> products =  this.productRepository.searchPrice(min, max);
		List<ListProductBasicInfo> result = new LinkedList<>(); 
		
		for(Product p : products) {
			ListProductBasicInfo prod = new ListProductBasicInfo(p.getProductId(), p.getTitle(), p.getPrice(), p.getDate());
			result.add(prod);
		}
		return result;
	}
	
	public List<ListProductBasicInfo> searchTitle(String str){
		str = "%" + str + "%";
		List<Product> products =  this.productRepository.search(str);
		List<ListProductBasicInfo> result = new LinkedList<>(); 
		
		for(Product p : products) {
			ListProductBasicInfo prod = new ListProductBasicInfo(p.getProductId(), p.getTitle(), p.getPrice(), p.getDate());
			result.add(prod);
		}
		return result;
	}
	

}
