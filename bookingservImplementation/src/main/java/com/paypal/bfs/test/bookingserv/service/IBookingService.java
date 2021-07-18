package com.paypal.bfs.test.bookingserv.service;

import java.util.List;

import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * Service class for Booking
 *
 */
public interface IBookingService {
  
	/**
	 * Creates new booking
	 * @param booking Booking object holding booking detail
	 * @return persisted booking record
	 */
	Booking createBooking(Booking booking);
	
	/**
	 * Gets all persisted bookings
	 * @return List of persisted bookings
	 */
	List<Booking> getAllBookings();
}
