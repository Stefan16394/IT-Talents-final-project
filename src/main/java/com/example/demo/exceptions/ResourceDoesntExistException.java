package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceDoesntExistException extends VMZoneException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 375757029683563266L;

	public ResourceDoesntExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceDoesntExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ResourceDoesntExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceDoesntExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceDoesntExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ResourceDoesntExistException(HttpStatus statusCode, String message) {
		super(statusCode, message);
		// TODO Auto-generated constructor stub
	}
	
	

	

}
