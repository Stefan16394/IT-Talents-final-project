package com.vmzone.demo.dto;

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
	private Long categoryId;
	private String title;
	private String information;
	private int inStock;
	private int delivery;
	private int quantity;
	private int inSale;
	private String detailedInformation;
	
}
