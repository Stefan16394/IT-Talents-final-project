package com.vmzone.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidEmailException extends VMZoneException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2566145594761560805L;
	
	public InvalidEmailException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public InvalidEmailException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidEmailException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
