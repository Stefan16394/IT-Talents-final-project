package com.vmzone.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;
import com.vmzone.demo.repository.ProductInSaleRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.service.ProductInSaleService;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ProductInSaleServiceTest {

	private static final AddProductInSaleDTO ADD_PRODUCT_IN_SALE_DTO = new AddProductInSaleDTO(1L,
			LocalDateTime.of(2018, Month.FEBRUARY, 3, 6, 30, 40, 50000),
			LocalDateTime.of(2019, Month.FEBRUARY, 3, 6, 30, 40, 50000), 10);
	
	private static Product TEST_PRODUCT = new Product(new Category("Shoes",null), "Product", "Information", 1, 24, 20, 0, "Details");

	private static final ProductInSale PRODUCT_IN_SALE = new ProductInSale(TEST_PRODUCT, LocalDateTime.of(2018, Month.FEBRUARY, 3, 6, 30, 40, 50000),
		LocalDateTime.of(2019, Month.FEBRUARY, 3, 6, 30, 40, 50000),10);


	@Mock
	private ProductInSaleRepository productInSaleRepository;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductInSaleService productInSaleService;
	
	@Before
	public void init() {
		TEST_PRODUCT = new Product(new Category("Shoes",null), "Product", "Information", 1, 24, 20, 0, "Details");
	}

	@Test(expected = ResourceDoesntExistException.class)
	public void testAddProductInSaleWhenProductIsAlreadyInSaleForDates() throws VMZoneException {
		
		when(productInSaleRepository.getProduct(ADD_PRODUCT_IN_SALE_DTO.getProductId(),
				ADD_PRODUCT_IN_SALE_DTO.getStartDate(), ADD_PRODUCT_IN_SALE_DTO.getEndDate(),
				ADD_PRODUCT_IN_SALE_DTO.getDiscountPercentage())).thenReturn(PRODUCT_IN_SALE);
		productInSaleService.addProductInSale(ADD_PRODUCT_IN_SALE_DTO);
	}
	
	@Test(expected = ResourceDoesntExistException.class)
	public void testAddProductInSaleWhenProductDoesntExist() throws VMZoneException {
		
		when(productInSaleRepository.getProduct(ADD_PRODUCT_IN_SALE_DTO.getProductId(),
				ADD_PRODUCT_IN_SALE_DTO.getStartDate(), ADD_PRODUCT_IN_SALE_DTO.getEndDate(),
				ADD_PRODUCT_IN_SALE_DTO.getDiscountPercentage())).thenReturn(null);
		productInSaleService.addProductInSale(ADD_PRODUCT_IN_SALE_DTO);
	}
	
	@Test
	public void testAddProductWithValidInput() throws VMZoneException {
		when(productInSaleRepository.getProduct(ADD_PRODUCT_IN_SALE_DTO.getProductId(),
				ADD_PRODUCT_IN_SALE_DTO.getStartDate(), ADD_PRODUCT_IN_SALE_DTO.getEndDate(),
				ADD_PRODUCT_IN_SALE_DTO.getDiscountPercentage())).thenReturn(null);
		
		when(productRepository.findById(ADD_PRODUCT_IN_SALE_DTO.getProductId())).thenReturn(Optional.of(TEST_PRODUCT));
		productInSaleService.addProductInSale(ADD_PRODUCT_IN_SALE_DTO);
		
		assertEquals(1,TEST_PRODUCT.getInSale());
	}

	@Configuration
	static class Config {

	}
}
