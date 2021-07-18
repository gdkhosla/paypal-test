package com.paypal.bfs.test.bookingserv.dal.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * DAL client for Booking entity
 *
 */
@Component
public interface IBookingClient {
	
	/**
	 * DAL client method to persist booking record
	 * @param booking Booking object to persist
	 * @return persisted booking record
	 */
	Booking createBooking(Booking booking);
	
	
	/**
	 * DAL client method to get all persisted booking
	 * records
	 * @return List of Booking records
	 */
	List<Booking> getAllBookings();

}
