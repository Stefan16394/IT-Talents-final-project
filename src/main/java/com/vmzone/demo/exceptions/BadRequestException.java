package com.vmzone.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends VMZoneException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2801635784103043372L;

	public BadRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(HttpStatus statusCode, String message) {
		super(statusCode, message);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BadRequestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
