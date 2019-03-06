package com.vmzone.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsDTO {
	String email;
	String text;
	
	@Override
	public String toString() {
		return "Този имейл " + email + " изпрати следното запитване: " + text;
	}

}
