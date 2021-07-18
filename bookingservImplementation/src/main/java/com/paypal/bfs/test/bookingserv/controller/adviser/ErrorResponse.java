package com.paypal.bfs.test.bookingserv.controller.adviser;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	
	private HttpStatus statusCode;
	
	private String exception;

}
