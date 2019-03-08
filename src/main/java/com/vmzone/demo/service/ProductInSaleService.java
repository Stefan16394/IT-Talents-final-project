package com.vmzone.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.dto.ListProductsInSale;
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
	
	
	public ProductInSale addProductInSale(AddProductInSaleDTO sale) throws VMZoneException {
		ProductInSale p = this.productInSaleRepository.getProduct(sale.getProductId(), sale.getStartDate(), sale.getEndDate(), sale.getDiscountPercentage());
		if(p != null) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product is already in sale for this dates");
		}
		Product prod = null;
		try {
			prod = this.productRepository.findById(sale.getProductId()).get();	
		}catch(NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist.");
		}
		
		prod.setInSale(1);
		ProductInSale newProductInSale = new ProductInSale(
				prod,
				sale.getStartDate(),
				sale.getEndDate(),
				sale.getDiscountPercentage()
				);
		
		return this.productInSaleRepository.save(newProductInSale);
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
