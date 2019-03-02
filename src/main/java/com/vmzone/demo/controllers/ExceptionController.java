package com.vmzone.demo.controllers;


import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.vmzone.demo.exceptions.VMZoneException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(VMZoneException.class)
	public void handleException(final VMZoneException e, HttpServletResponse res) throws VMZoneException {
		
		throw e;
	}
}
