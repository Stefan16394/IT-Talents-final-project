package com.vmzone.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vmzone.demo.dto.AddCategoryDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.service.CategoryService;

@RestController
public class CategoryController {
	@Autowired
	public CategoryService categoryService;

	@GetMapping("/categories")
	public List<ListCategory> getAllMainCategories() {
		return this.categoryService.getAllMainCategories();
	}

	@PostMapping("/category")
	public void createCategory(@RequestBody AddCategoryDTO category) {
		this.categoryService.createCategory(category);
	}

	@GetMapping("/category/{id}")
	public ListCategory getCategoryById(@PathVariable("id") long id) {
		ListCategory category = this.categoryService.getCategoryById(id);
		return category;
	}
}
