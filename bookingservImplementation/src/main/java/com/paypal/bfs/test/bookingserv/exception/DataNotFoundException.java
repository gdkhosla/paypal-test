package com.paypal.bfs.test.bookingserv.exception;

/**
 * Exception to be thrown when data not found
 *
 */
public class DataNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -1122309734205616543L;

	public DataNotFoundException() {
		super();
	}
	
	public DataNotFoundException(String message) {
		super(message);
	}

}
