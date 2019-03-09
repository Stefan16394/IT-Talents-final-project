package com.vmzone.demo.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddCategoryDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.dto.ListFinalSubCategories;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.User;
import com.vmzone.demo.service.CategoryService;
import com.vmzone.demo.utils.SessionManager;

@RestController
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public List<ListCategory> getAllMainCategories() {
		return this.categoryService.getAllMainCategories();
	}

	@PostMapping("/category")
	public Category createCategory(@RequestBody @Valid AddCategoryDTO category, HttpSession session) throws ResourceDoesntExistException, BadCredentialsException, ResourceAlreadyExistsException {
		if (!SessionManager.isUserLoggedIn(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED, "You are not logged in! You should log in first!");
		}
		if(!SessionManager.isAdmin(session)) {
			throw new BadCredentialsException(HttpStatus.UNAUTHORIZED,"You do not have access to this feature!");
		}
		return this.categoryService.createCategory(category);
	}
	
	@GetMapping("/category/subcats/{id}")
	public List<ListFinalSubCategories> getLeafCategories(@PathVariable("id") long id){
		return this.categoryService.getLeafCategories(id);
	}
}
