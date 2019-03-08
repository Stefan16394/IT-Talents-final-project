package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddCharacteristicDTO;
import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.AddProductInSaleDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListProductsInSale;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.ProductInSaleService;
import com.vmzone.demo.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductInSaleService productInSaleService;
	
	@PostMapping("/product")
	public void addProduct(@RequestBody AddProductDTO product, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		
		this.productService.addProduct(product);
	}
	@PostMapping("/product/characteristic/{id}")
	public void addCharacteristicToProduct(@PathVariable("id") Long productId, @RequestBody AddCharacteristicDTO characteristic, HttpSession session ) throws ResourceDoesntExistException, BadCredentialsException, ResourceAlreadyExistsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		
		this.productService.addCharacteristicForProduct(productId, characteristic);
	}
	@PostMapping("/product/remove/characteristic/{prodId}/{charId}")
	public void removeCharacteristicForProduct(@PathVariable("prodId") Long prodId, @PathVariable("charId") Long charId , HttpSession session ) throws ResourceDoesntExistException, BadCredentialsException, ResourceAlreadyExistsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		
		this.productService.removeCharacteristicForProduct(prodId, charId);
	}
	
	// http://localhost:8080/product/?categoryId=1 Така се тества!
	@GetMapping("/product")
	public List<ListProductBasicInfo> getAllproductsForCategory(@RequestParam("categoryId") long id) {
		return this.productService.getAllproducts(id);
	}
	//TODO fix dto
	@GetMapping("/productsQuantity")
	public List<ListProductBasicInfo> getAllProductsWithSmallQuantity() {
		return this.productService.getAllProductsWithSmallQuantity();
	}
	
	@PutMapping("/product/remove/{id}")
	public void removeProduct(@PathVariable long id, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		 this.productService.removeProductById(id);
	}
	
	@PutMapping("/product/edit/{id}")
	public void editProduct(@PathVariable long id, @RequestBody EditProductDTO product, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		
		this.productService.editProduct(id,product);
	}
	
	@GetMapping("/products/{id}")
	public List<ListReview> getReviewsForAProduct(@PathVariable long id) {
		return this.productService.getReviewsForProduct(id);
	}
	//TODO not working for products without reviews
	@GetMapping("/products")
	public List<ListProduct> getAllproducts() {
		return this.productService.getAllproducts();
	}
	
	//TODO not working for products without reviews
	@GetMapping("/info/{id}")
	public ListProduct getAllInfoForProduct(@PathVariable long id) throws BadCredentialsException {
		return this.productService.getAllInfoForProduct(id);
	}
	//TODO needs to be done properly
		@GetMapping("/productsSort")
		public List<ListProductBasicInfo> getAllproducts(
				@RequestParam(name="sortBy", required=false) String sortBy,
				@RequestParam(name="categoryId", required=false) Long categoryId) {
			return this.productService.getAllproducts(sortBy, categoryId);
		}
	//TODO must be a thread
	@PostMapping("/calculate")
	public void calculateRating(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		this.productService.calculateRating();
	}
	
	@PostMapping("/sale")
	public void addProductInSale(@RequestBody AddProductInSaleDTO product, HttpSession session) throws VMZoneException {
		if (session.getAttribute("user") == null) {
			throw new ResourceDoesntExistException("You are not logged in! You should log in first!");
		}
		if(!((User) session.getAttribute("user")).isAdmin()) {
			throw new BadCredentialsException("You do not have access to this feature!");
		}
		this.productInSaleService.addProductInSale(product);
	}
	@GetMapping("/sales")
	public List<ListProductsInSale> getAllProductsInsale() {
		return this.productInSaleService.showProductsInSale();
	}
	
	@GetMapping("/searchPrice")
	public List<ListProductBasicInfo> searchPrice(@RequestParam("min") double min , @RequestParam("max") double max){
		return this.productService.searchPrice(min, max);
	}
	
	@GetMapping("/search")
	public List<ListProductBasicInfo> search(@RequestParam("search") String str){
		return this.productService.searchTitle(str);
	}
	
	@GetMapping("/filter/colour")
	public List<ListProductBasicInfo> searchByColour(
			@RequestParam(name="sortBy", required=false) String sortBy,
			@RequestParam(name="categoryId", required=false) Long categoryId){
		return this.productService.sortCharacteristicsByColour(sortBy, categoryId);
	}
	
	@GetMapping("/filter/size")
	public List<ListProductBasicInfo> searchBySize(
			@RequestParam(name="sortBy", required=false) String sortBy,
			@RequestParam(name="categoryId", required=false) Long categoryId){
		return this.productService.sortCharacteristicsBySize(sortBy, categoryId);
	}

	
	
	
		
}
