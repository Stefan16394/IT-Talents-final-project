package com.vmzone.demo.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
	
	@NotBlank(message="Password should not be empty")
	@Size(min = 4, max = 35, message="password should be with minimum of 4 characters and maximum of 35")
	String password;

}
