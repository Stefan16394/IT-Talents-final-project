package com.vmzone.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@PostMapping("/product")
	public void addProduct(@RequestBody AddProductDTO product) {
		this.productService.addProduct(product);
	}
}
