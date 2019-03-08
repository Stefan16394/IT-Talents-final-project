package com.vmzone.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProductDTO {
	private Long categoryId;
	private String title;
	private String information;
	private int inStock;
	private int delivery;
	private int quantity;
	private int isDeleted;
	private int inSale;
	private String detailedInformation;
}
