package com.vmzone.demo.controllers;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.vmzone.demo.dto.RegisterDTO;

public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		RegisterDTO user = (RegisterDTO) obj;
		if (user.getFirstName() == null) {
			e.rejectValue("firstName", "400", "first name should not be empty");
		}
		
		// TODO validate fields with regex...
		
//	        if (p.getAge() < 0) {
//	            e.rejectValue("age", "negativevalue");
	
	}

}
