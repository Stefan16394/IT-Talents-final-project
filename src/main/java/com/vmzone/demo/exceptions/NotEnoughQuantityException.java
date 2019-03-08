package com.vmzone.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughQuantityException extends VMZoneException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7963296352857471486L;
	
	public NotEnoughQuantityException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public NotEnoughQuantityException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotEnoughQuantityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughQuantityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughQuantityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughQuantityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
