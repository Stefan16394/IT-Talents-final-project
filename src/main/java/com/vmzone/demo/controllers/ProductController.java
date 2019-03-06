package com.vmzone.demo.controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListProductsInSale;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.service.ProductInSaleService;
import com.vmzone.demo.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductInSaleService productInSaleService;
	
	@PostMapping("/product")
	public void addProduct(@RequestBody AddProductDTO product) {
		this.productService.addProduct(product);
	}
	
	// http://localhost:8080/product/?categoryId=1 Така се тества!
	@GetMapping("/product")
	public List<ListProductBasicInfo> getAllproductsForCategory(@RequestParam("categoryId") long id) {
		return this.productService.getAllproducts(id);
	}
	
	@GetMapping("/productsQuantity")
	public List<ListProduct> getAllProductsWithSmallQuantity() {
		return this.productService.getAllProductsWithSmallQuantity();
	}
	
	@PutMapping("/product/remove/{id}")
	public void removeProduct(@PathVariable long id) throws ResourceDoesntExistException {
		 this.productService.removeProductById(id);
	}
	
	@PutMapping("/product/edit/{id}")
	public void editProduct(@PathVariable long id,@RequestBody EditProductDTO product) throws ResourceDoesntExistException {
			this.productService.editProduct(id,product);
	}
	
	@GetMapping("/products/{id}")
	public List<ListReview> getReviewsForAProduct(@PathVariable long id) {
		return this.productService.getReviewsForProduct(id);
	}
	@GetMapping("/products")
	public List<ListProduct> getAllproducts() {
		return this.productService.getAllproducts();
	}
	//TODO needs to be done properly
	@GetMapping("/productsSort")
	public List<ListProductBasicInfo> getAllproducts(
			@RequestParam(name="sortBy", required=false) String sortBy,
			@RequestParam(name="categoryId", required=false) Long categoryId) {
		return this.productService.getAllproducts(sortBy, categoryId);
	}

	@GetMapping("/info/{id}")
	public ListProduct getAllInfoForProduct(@PathVariable long id) throws BadCredentialsException {
		return this.productService.getAllInfoForProduct(id);
	}

	@PostMapping("/calculate")
	public void calculateRating() throws ResourceDoesntExistException {
		this.productService.calculateRating();
	}
	
	@PostMapping("/sale")
	public void addProductInSale(@RequestBody AddProductInSaleDTO product) throws VMZoneException {
		this.productInSaleService.addProductInSale(product);
	}
	@GetMapping("/sales")
	public List<ListProductsInSale> getAllProductsInsale() {
		return this.productInSaleService.showProductsInSale();
	}
	
	
	
		
}
