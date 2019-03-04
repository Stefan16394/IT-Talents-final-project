package com.vmzone.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListFinalSubCategories;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.models.Category;
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
	private CategoryService categoryService;

	@Autowired
	private ReviewRepository reviewRepository;

	public void addProduct(AddProductDTO product) {
		System.out.println(product);
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

	public void removeProductById(long id) {
		Product product = this.productRepository.findById(id).get();
		product.setIsDeleted(1);
		this.productRepository.save(product);
	}

	public Product getProductById(long id) {
		return this.productRepository.findById(id).get();
	}

	public void editProduct(long id, EditProductDTO editedProduct) {
		Product product = this.productRepository.findById(id).get();

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
}
