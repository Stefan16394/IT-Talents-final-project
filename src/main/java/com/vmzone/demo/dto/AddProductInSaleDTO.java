package com.vmzone.demo.dto;

import java.time.LocalDateTime;

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
public class AddProductInSaleDTO {
	
	@NotNull(message="the id of the product should not be empty")
	@Positive
	private Long productId;
	@NotBlank(message="the start date should not be empty")
	private LocalDateTime startDate;
	@NotNull(message="the end date should not be empty")
	private LocalDateTime endDate;
	@NotNull(message="the discount percentage should not be empty")
	@Positive
	private int discountPercentage;

}
