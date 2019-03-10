package com.vmzone.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AddCharacteristicDTO {
	
	@NotBlank(message="the name of the characteristic should not be empty")
	private String name;
	@NotBlank(message="the value of the characteristic should not be empty")
	private String value;

}
