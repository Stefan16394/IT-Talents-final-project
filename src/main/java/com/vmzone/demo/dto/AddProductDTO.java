package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private LocalDateTime date;
	private int quantity;
	private int inSale;
	private String detailedInformation;
}
