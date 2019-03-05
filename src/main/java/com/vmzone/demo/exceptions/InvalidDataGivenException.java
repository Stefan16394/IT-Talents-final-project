package com.vmzone.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataGivenException extends VMZoneException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7704742948577921747L;
	
	public InvalidDataGivenException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public InvalidDataGivenException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidDataGivenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidDataGivenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidDataGivenException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidDataGivenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
