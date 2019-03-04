package com.vmzone.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@PostMapping("/product")
	public void addProduct(@RequestBody AddProductDTO product) {
		this.productService.addProduct(product);
	}
	
	
	// http://localhost:8080/product/?categoryId=1 Така се тества!
	@GetMapping("/product")
	public List<ListProductBasicInfo> getAllproductsForCategory(@RequestParam("categoryId") long id) {
		return this.productService.getAllproducts(id);
	}
	
	@PutMapping("/product/remove/{id}")
	public void removeProduct(@PathVariable long id) {
		 this.productService.removeProductById(id);
	}
	
	@PutMapping("/product/edit/{id}")
	public void editProduct(@PathVariable long id,@RequestBody EditProductDTO product) {
			this.productService.editProduct(id,product);
	}
	
	@GetMapping("/products/{id}")
	public List<ListReview> getReviewsForAProduct(@PathVariable long id) {
		return this.productService.getReviewsForProduct(id);
	}

	@GetMapping("/info/{id}")
	public ListProduct getAllInfoForProduct(@PathVariable long id) throws BadCredentialsException {
		return this.productService.getAllInfoForProduct(id);
	}
			
}
