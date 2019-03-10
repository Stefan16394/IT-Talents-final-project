package com.vmzone.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
	
	@Email
	private String email;
	
	@NotBlank(message = "password should be at least 4 characters")
    @Size(min = 4, max = 50, message="password should be at least 4 characters")
	private String password;
}
