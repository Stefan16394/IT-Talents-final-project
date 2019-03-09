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
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddProductDTO {
	@NotNull(message="you should choose in which category the product will be")
	@Positive
	private Long categoryId;
	@NotBlank(message="the title of the product should not be empty")
	private String title;
	@NotBlank(message="the information of the product should not be empty")
	private String information;
	@Min(0)
	@Max(1)
	private int inStock;
	@NotNull(message="the delivery should not be empty")
	@Positive
	private int delivery;
	@NotNull(message="the quantity should not be empty")
	@Positive
	private int quantity;
	@Min(0)
	@Max(1)
	private int inSale;
	private String detailedInformation;
	
}
