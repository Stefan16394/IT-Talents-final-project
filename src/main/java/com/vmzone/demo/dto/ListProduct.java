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

	public ListProduct(Long id, String title, String information, int inStock, int delivery, String detailedInformation) {
		super();
		this.id = id;
		this.title = title;
		this.information = information;
	}
	
	public void fillReviews(List<ListReview> reviews) {
		if(reviews != null && !reviews.isEmpty()) {
			this.reviews = new LinkedList<ListReview>(reviews);
		}
	}
	
	public List<ListReview> getReviews(){
		return Collections.unmodifiableList(this.reviews);
	}
	

}
