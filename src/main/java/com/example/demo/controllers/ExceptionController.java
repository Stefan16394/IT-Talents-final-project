package com.example.demo.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.VMZoneException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(VMZoneException.class)
	public void handleException(final VMZoneException e, HttpServletResponse res) throws VMZoneException {
		
		throw e;
	}
}
