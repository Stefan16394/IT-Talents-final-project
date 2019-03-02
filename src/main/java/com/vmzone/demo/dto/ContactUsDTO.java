package com.vmzone.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
