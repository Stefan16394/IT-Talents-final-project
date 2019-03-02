package com.vmzone.demo.dto;

import java.util.List;

import com.vmzone.demo.models.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListCategory {
	private Long id;
	private String name;
	private Category parent;
	private List<ListSubCategory> subCategories;
}
