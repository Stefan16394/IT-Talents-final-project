package com.vmzone.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.dto.ListProductsInSale;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;
import com.vmzone.demo.repository.ProductInSaleRepository;
import com.vmzone.demo.repository.ProductRepository;
/**
 * Service layer communicating with product repository and product in sale repository for managing requests for products in sale
 * 
 * @author Stefan Rangelov and Sabiha Djurina
 *
 */

@Service
public class ProductInSaleService {
	
	@Autowired
	public ProductInSaleRepository productInSaleRepository;
	
	@Autowired
	public ProductRepository productRepository;
	
	/**
	 * adding existing product in sale
	 * 
	 * @param sale - dto object with info for added sale
	 * @return ProductInSale - newely added sale
	 * @throws ResourceDoesntExistException - when product is already in sale or does not exist in db
	 */
	
	public ProductInSale addProductInSale(AddProductInSaleDTO sale) throws ResourceDoesntExistException {
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
	
	@Scheduled(fixedRate = 7*24*60*60000)
	public void deleteExpiredPromotions() {
		LocalDateTime dateNow = LocalDateTime.now();
		this.productInSaleRepository.deleteExpired(dateNow);
	}
	
	public void deletePromotion(long id) {
		this.productInSaleRepository.deletePromotion(id);
	}

}
