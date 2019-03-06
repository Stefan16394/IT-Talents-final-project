package com.vmzone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCategoryDTO {
	private String name;
	private Long parent_id;
}
