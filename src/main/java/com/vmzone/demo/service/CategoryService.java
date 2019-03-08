package com.vmzone.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddCategoryDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.dto.ListFinalSubCategories;
import com.vmzone.demo.dto.ListSubCategory;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
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


	public void createCategory(AddCategoryDTO category) throws ResourceAlreadyExistsException {
		Category checkExists = this.categoryRepository.findByName(category.getName());
		if(checkExists != null) {
			throw new ResourceAlreadyExistsException("This category already exists!");
		}
		Category newCategory = new Category(category.getName(), category.getParent_id() == null ? null
				: this.categoryRepository.findById(category.getParent_id()).get());
		this.categoryRepository.save(newCategory);
	}

	public List<ListSubCategory> getSubcategoriesForCategory(long id) {
		return this.categoryRepository.findAll().stream()
				.filter(cat -> cat.getParent() != null && cat.getParent().getCategoryId().equals(id))
				.map(subCat -> new ListSubCategory(subCat.getCategoryId(), subCat.getName()))
				.collect(Collectors.toList());
	}
	
	public List<ListFinalSubCategories> getLeafCategories(long id){

		List<ListFinalSubCategories> subCats = new ArrayList<>();
		List string = this.categoryRepository.getLeafCategories(id);
		for(Object o:string) {
			Object[] rows = (Object[]) o;
			subCats.add(new ListFinalSubCategories(Long.parseLong(rows[0].toString()),rows[1].toString()));
		}
		return subCats;
	}
}
