package com.vmzone.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgottenPasswordDTO {
	
	@NotBlank
	@Email
	private String email;

}
