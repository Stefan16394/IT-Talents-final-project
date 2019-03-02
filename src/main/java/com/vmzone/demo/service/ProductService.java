package com.vmzone.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.repository.CategoryRepository;
import com.vmzone.demo.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public void addProduct(AddProductDTO product ) {
		System.out.println(product);
		Product newProduct = new Product(
				this.categoryRepository.findById(product.getCategoryId()).get(),
				product.getTitle(),product.getInformation(),product.getInStock(),
				product.getDelivery(),product.getDate(),product.getQuantity(),
				product.getInSale(),product.getDetailedInformation()
				);
		this.productRepository.save(newProduct);
	}
}
