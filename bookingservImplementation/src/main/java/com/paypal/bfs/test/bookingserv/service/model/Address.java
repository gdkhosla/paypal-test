package com.paypal.bfs.test.bookingserv.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Service model representing address
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Address {

	private String id;
	
	private String line1;
	
	private String line2;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String zipCode;
	
	private Booking booking;

}
