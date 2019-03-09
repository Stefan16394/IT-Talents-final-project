package com.vmzone.demo.dto;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListProduct {
	
	private Long id;
	private String title;
	private String information;
	private int inStock;
	private int delivery;
	private String detailedInformation;
	private Double rating;
	
	private List<ListReview> reviews = new LinkedList<>();
	private List<AddCharacteristicDTO> characteristics = new LinkedList<>();

	public ListProduct(Long id, String title, String information, int inStock, int delivery, String detailedInformation, Double rating) {
		super();
		this.id = id;
		this.title = title;
		this.information = information;
		this.inStock = inStock;
		this.delivery = delivery;
		this.detailedInformation = detailedInformation;
		this.rating = rating;
	}
	
	public void fillReviews(List<ListReview> reviews) {
		if(reviews != null && !reviews.isEmpty()) {
			this.reviews = new LinkedList<ListReview>(reviews);
		}
	}
	
	public List<ListReview> getReviews(){
		return Collections.unmodifiableList(this.reviews);
	}

	public void fillCharacteristics(List<AddCharacteristicDTO> characteristics) {
		if(characteristics != null && !characteristics.isEmpty()) {
			this.characteristics = new LinkedList<AddCharacteristicDTO>(characteristics);
		}
		
	}
	

}
