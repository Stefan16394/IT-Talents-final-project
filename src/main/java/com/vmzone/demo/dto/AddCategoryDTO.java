package com.vmzone.demo.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long category_id;
	private String name;
	private Long parent_id;
}
