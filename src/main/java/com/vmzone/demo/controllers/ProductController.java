package com.vmzone.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	
	@PostMapping("/product")
	public void addProduct(@RequestBody AddProductDTO product) {
		this.productService.addProduct(product);
	}
	
	@GetMapping("/products")
	public List<ListProduct> getAllproducts() {
		return this.productService.getAllproducts();
	}
	
	@PutMapping("/product/remove/{id}")
	public void removeProduct(@PathVariable long id) {
		 this.productService.removeProductById(id);
	}
	
	@PutMapping("/product/edit/{id}")
	public void editProduct(@PathVariable long id,@RequestBody EditProductDTO product) {
			this.productService.editProduct(id,product);
	}
	
	@GetMapping("product/{id}")
	public Product getProductById(@PathVariable long id) {
		return this.productService.getProductById(id);
	}
	
	@GetMapping("/products/{id}")
	public List<ListReview> getReviewsForAProduct(@PathVariable long id) {
		return this.productService.getReviewsForProduct(id);
	}
	
	
}
