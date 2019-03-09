package com.vmzone.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsDTO {
	@Email
	String email;
	@NotBlank
	String text;
	
	@Override
	public String toString() {
		return "Този имейл " + email + " изпрати следното запитване: " + text;
	}

}
