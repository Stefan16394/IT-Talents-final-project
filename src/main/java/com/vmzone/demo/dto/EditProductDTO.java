package com.vmzone.demo.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProductDTO {
	@NotNull
	@Positive
	private Long categoryId;
	@NotBlank
	private String title;
	@NotBlank
	private String information;
	@Min(0)
	@Max(1)
	private int inStock;
	@NotNull
	@Positive
	private int delivery;
	@NotNull
	@Positive
	private int quantity;
	@NotNull
	@Min(0)
	@Max(1)
	private int isDeleted;
	@NotNull
	@Min(0)
	@Max(1)
	private int inSale;
	private String detailedInformation;
}
