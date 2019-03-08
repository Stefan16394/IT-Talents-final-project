package com.vmzone.demo;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import com.vmzone.demo.repository.ProductInSaleRepository;
import com.vmzone.demo.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ProductInSaleServiceTest {
	
	@Autowired
	public ProductInSaleRepository productInSaleRepository;
	
	@Autowired
	public ProductRepository productRepository;
	
	@Configuration
	static class Config {

	}
}
