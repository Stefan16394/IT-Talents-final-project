package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddProductInSaleDTO {
	
	private Long productId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int discountPercentage;

}
