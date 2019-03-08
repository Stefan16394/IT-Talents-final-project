package com.vmzone.demo.dto;

import java.time.LocalDateTime;

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
	
	private Long productId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int discountPercentage;

}
