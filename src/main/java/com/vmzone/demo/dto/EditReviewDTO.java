package com.vmzone.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditReviewDTO {
	
	private String review;
	private int rating;
	private int isDeleted;

}
