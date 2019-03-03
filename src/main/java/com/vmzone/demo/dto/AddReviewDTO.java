package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddReviewDTO {
	
	private Long productId;
	private Long userId;
	
	private String review;
	private int rating;
	private LocalDateTime date;

}
