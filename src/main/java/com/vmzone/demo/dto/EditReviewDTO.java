package com.vmzone.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EditReviewDTO {
	
	private String review;
	private int rating;
	private int isDeleted;

}
