package com.vmzone.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ListProductsInSale {

	private String title;
	private String information;
	
	
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int dicountPercentage;


}
