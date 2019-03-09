package com.vmzone.demo.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewDTO {
	@NotNull(message ="product id should not be empty")
	private Long productId;
	@NotNull(message = "review should not be empty")
	@Size(min = 3, max = 200,message = "review should contain between 3 and 200 symbols")
	private String review;
	
	@Min(1)
	@Max(5)
	private int rating;
}
