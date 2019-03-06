package com.vmzone.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
