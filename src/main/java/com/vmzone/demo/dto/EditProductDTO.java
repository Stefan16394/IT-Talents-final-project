package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
