package com.vmzone.demo;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.vmzone.demo.dto.AddCategoryDTO;
import com.vmzone.demo.dto.ListCategory;
import com.vmzone.demo.dto.ListSubCategory;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.repository.CategoryRepository;
import com.vmzone.demo.service.CategoryService;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class CategoryServiceTests {
	private static final AddCategoryDTO ADD_CATEGORY_DTO = new AddCategoryDTO("Category",null);

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	public void testGetAllMainCategories() {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(1L, "Shoes", null));
		categories.add(new Category(2L, "Shirts", new Category(4L, "SubCat", null)));
		categories.add(new Category(3L, "Pants", null));

		when(categoryRepository.findAll()).thenReturn(categories);

		List<ListCategory> result = categoryService.getAllMainCategories();

		final int EXPECTED_RESULT = 2;
		assertEquals(EXPECTED_RESULT, result.size());
	}

	@Test(expected = ResourceAlreadyExistsException.class)
	public void testCreateCategoryWhenCategoryAlreadyExists() throws ResourceAlreadyExistsException {
		when(categoryRepository.findByName(ADD_CATEGORY_DTO.getName())).thenReturn(new Category());
		categoryService.createCategory(ADD_CATEGORY_DTO);
	}
	
	@Test
	public void testCreateCategoryWhenCategoryDoesntExist() throws ResourceAlreadyExistsException {
		Category category = new Category(1L, ADD_CATEGORY_DTO.getName(), null);
		when(categoryRepository.findByName(ADD_CATEGORY_DTO.getName())).thenReturn(null);

		when(categoryRepository.save(category)).thenReturn(category);

		categoryService.createCategory(ADD_CATEGORY_DTO);
	}
	
	@Test
	public void testGetSubcategoriesForCategory() {
		Category mainCategory = new Category(1L,"Main",null);
		List<Category> subCategories = new ArrayList<>();
		subCategories.add(new Category(2L,"SubCat1", mainCategory));
		subCategories.add(new Category(3L,"SubCat1", mainCategory));
		subCategories.add(new Category(4L,"SubCat1", null));
		
		when(categoryRepository.findAll()).thenReturn(subCategories);
		List<ListSubCategory> result=categoryService.getSubcategoriesForCategory(mainCategory.getCategoryId());
		final int expected = 2;
		assertEquals(expected,result.size());
	}
}
