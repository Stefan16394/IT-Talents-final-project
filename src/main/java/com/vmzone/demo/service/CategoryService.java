package com.vmzone.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddCategoryDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.dto.ListSubCategory;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	public CategoryRepository categoryRepository;

	public List<ListCategory> getAllMainCategories() {

		List<ListCategory> mainCategories = this.categoryRepository.findAll().stream()
				.filter(c -> c.getParent() == null).map(c -> new ListCategory(c.getCategoryId(), c.getName(),
						c.getParent(), this.getSubcategoriesForCategory(c.getCategoryId())))
				.collect(Collectors.toList());

		return mainCategories;
	}

	public ListCategory getCategoryById(long id) {

		ListCategory categ = this.categoryRepository.findById(id).map(c -> new ListCategory(c.getCategoryId(),
				c.getName(), c.getParent(), this.getSubcategoriesForCategory(c.getCategoryId()))).get();
		return categ;
	}

	public void createCategory(AddCategoryDTO category) {
		Category newCategory = new Category(category.getCategory_id(), category.getName(),
				this.categoryRepository.findById(category.getParent_id()).get());
		this.categoryRepository.save(newCategory);
	}

	public List<ListSubCategory> getSubcategoriesForCategory(long id) {
		return this.categoryRepository.findAll().stream()
				.filter(cat -> cat.getParent() != null && cat.getParent().getCategoryId().equals(id))
				.map(subCat -> new ListSubCategory(subCat.getCategoryId(), subCat.getName()))
				.collect(Collectors.toList());
	}
}
