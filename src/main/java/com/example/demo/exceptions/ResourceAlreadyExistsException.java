package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends VMZoneException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4903742619812640717L;
	

	public ResourceAlreadyExistsException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public ResourceAlreadyExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourceAlreadyExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public ResourceAlreadyExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ResourceAlreadyExistsException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ResourceAlreadyExistsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
 
}
