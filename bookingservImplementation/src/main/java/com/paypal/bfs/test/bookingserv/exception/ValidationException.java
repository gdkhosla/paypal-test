package com.paypal.bfs.test.bookingserv.exception;

/**
 * Exception to be thrown when validation error messages
 *
 */
public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -1122309734205616543L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(String message) {
		super(message);
	}

}
