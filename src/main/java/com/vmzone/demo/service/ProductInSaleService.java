package com.vmzone.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.dto.ListProductsInSale;
import com.vmzone.demo.exceptions.InvalidDataGivenException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;
import com.vmzone.demo.repository.ProductInSaleRepository;
import com.vmzone.demo.repository.ProductRepository;

@Service
public class ProductInSaleService {
	
	@Autowired
	public ProductInSaleRepository productInSaleRepository;
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;
	
	public void addProductInSale(AddProductInSaleDTO sale) throws VMZoneException {
		Product p = this.productService.productRepository.findById(sale.getProductId()).get();
		if(p == null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}
		
		if(this.productInSaleRepository.findFirstByStartDate(sale.getStartDate()) != null 
				&& this.productInSaleRepository.findFirstByEndDate(sale.getEndDate()) != null
				&& this.productInSaleRepository.findFirstByProducts(this.productService.productRepository.findById(sale.getProductId()).get()) != null) {
			throw new InvalidDataGivenException(HttpStatus.BAD_REQUEST, "Product in sale already exist");
		}
		p.setInSale(1);
		ProductInSale newProductInSale = new ProductInSale(
				this.productService.productRepository.findById(sale.getProductId()).get(),
				sale.getStartDate(),
				sale.getEndDate(),
				sale.getDiscountPercentage()
				);
		
		this.productInSaleRepository.save(newProductInSale);
	}
	
	public List<ListProductsInSale> showProductsInSale() {
		LocalDateTime dateNow = LocalDateTime.now();
		return this.productInSaleRepository.findAll().stream()
				.filter(product -> product.getProducts() != null
				&& product.getSaleId() != null 
				&& product.getStartDate().isBefore(dateNow)
				&& product.getEndDate().isAfter(dateNow))
				.map(p -> new ListProductsInSale(p.getProducts().getTitle(), p.getProducts().getInformation(), p.getStartDate(), p.getEndDate(), p.getDiscountPercentage()))
				.collect(Collectors.toList());
	}

}
