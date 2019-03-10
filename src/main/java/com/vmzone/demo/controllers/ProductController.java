package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.vmzone.demo.dto.HomePageDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListProductsInSale;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.exceptions.VMZoneException;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.models.ProductInSale;
import com.vmzone.demo.service.CategoryService;
import com.vmzone.demo.service.ProductInSaleService;
import com.vmzone.demo.service.ProductService;
import com.vmzone.demo.utils.SessionManager;

/**
 * Rest Controller for managing products requests
 * 
 * @author Sabiha Djurina and Stefan Rangelov
 * 
 *
 */

@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductInSaleService productInSaleService;
	
	@GetMapping("/home")
	public HomePageDTO homePage() {
		List<ListCategory> categories= this.categoryService.getAllMainCategories();
		List<ListProduct> products = this.getAllproducts();
		return new HomePageDTO(categories,products);
	}
	
	@PostMapping("/product")
	public Product addProduct(@RequestBody @Valid AddProductDTO product, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		
		return this.productService.addProduct(product);
	}
	@PostMapping("/product/characteristic/{id}")
	public long addCharacteristicToProduct(@PathVariable("id") Long productId, @RequestBody @Valid AddCharacteristicDTO characteristic, HttpSession session ) throws ResourceDoesntExistException, BadCredentialsException, ResourceAlreadyExistsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		
		return this.productService.addCharacteristicForProduct(productId, characteristic);
	}
	@PostMapping("/product/remove/characteristic/{prodId}/{charId}")
	public void removeCharacteristicForProduct(@PathVariable("prodId") Long prodId, @PathVariable("charId") Long charId , HttpSession session ) throws ResourceDoesntExistException, BadCredentialsException, ResourceAlreadyExistsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
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
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		 this.productService.removeProductById(id);
	}
	
	@PutMapping("/product/edit/{id}")
	public Product editProduct(@PathVariable long id, @RequestBody @Valid EditProductDTO product, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		
		return this.productService.editProduct(id,product);
	}
	
	@GetMapping("/products/{id}")
	public List<ListReview> getReviewsForAProduct(@PathVariable long id) {
		return this.productService.getReviewsForProduct(id);
	}

	@GetMapping("/products")
	public List<ListProduct> getAllproducts() {
		return this.productService.getAllproducts();
	}
	
	
	@GetMapping("/info/{id}")
	public ListProduct getAllInfoForProduct(@PathVariable long id) throws BadCredentialsException {
		return this.productService.getAllInfoForProduct(id);
	}
	
		@GetMapping("/productsSort")
		public List<ListProductBasicInfo> getAllproducts(
				@RequestParam(name="sortBy", required=false) String sortBy,
				@RequestParam(name="categoryId", required=false) Long categoryId,
				@RequestParam(name ="minPrice",required = false) Double min ,
				@RequestParam(name ="maxPrice",required = false) Double max) {
			return this.productService.getAllproducts(sortBy, categoryId,min,max);
		}
		
	@PostMapping("/calculate")
	public void calculateRating(HttpSession session) throws ResourceDoesntExistException, BadCredentialsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		this.productService.calculateRating();
	}
	
	@PostMapping("/sale")
	public ProductInSale addProductInSale(@RequestBody @Valid AddProductInSaleDTO product, HttpSession session) throws VMZoneException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new ResourceDoesntExistException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		return this.productInSaleService.addProductInSale(product);
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
