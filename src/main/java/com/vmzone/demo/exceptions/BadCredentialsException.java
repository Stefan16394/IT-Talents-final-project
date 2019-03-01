package com.vmzone.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends VMZoneException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1421596159044470575L;
	
	public BadCredentialsException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public BadCredentialsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public BadCredentialsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
