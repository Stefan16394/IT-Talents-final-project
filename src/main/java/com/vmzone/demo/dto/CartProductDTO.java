package com.vmzone.demo.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
public class CartProductDTO {
	@NotNull(message = "product id should not be empty")
	private Long productId;
	
	@NotNull(message = "quantity should not be empty")
	@Min(1)
	@Max(50)
	private int quantity;
}
