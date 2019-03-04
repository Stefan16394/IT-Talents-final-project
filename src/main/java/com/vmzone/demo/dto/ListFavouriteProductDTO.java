package com.vmzone.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ListFavouriteProductDTO {
	
	private String title;
	private String information;
	private Double rating;

}
