package com.paypal.bfs.test.bookingserv.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Service model representing booking records
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Booking {

	private Integer id;

	private String firstName;

	private String lastName;

	private Long dateOfBirth;

	private Long checkIn;

	private Long checkOut;

	private Double totalPrice;

	private Double deposit;

	private Address address;
}
