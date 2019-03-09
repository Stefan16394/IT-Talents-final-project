package com.vmzone.demo.dto;



import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCategoryDTO {
	@NotBlank(message = "the name for the category should not be empty")
	private String name;
	private Long parent_id;
}
