package com.paypal.bfs.test.bookingserv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paypal.bfs.test.bookingserv.dal.client.IBookingClient;
import com.paypal.bfs.test.bookingserv.service.model.Booking;
import com.paypal.bfs.test.bookingserv.validate.BookingRequestValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for booking service
 *
 */
@Component
@Slf4j
public class BookingService implements IBookingService{
	
	@Autowired
	private BookingRequestValidator bookingRequestValidator;
	
	@Autowired
	private IBookingClient bookingClient;

	@Override
	public Booking createBooking(Booking booking) {
		log.debug("Creating booking.");
		
		bookingRequestValidator.validateBookingRequest(booking);
		
		return bookingClient.createBooking(booking);
		
	}

	@Override
	public List<Booking> getAllBookings() {
		log.debug("Fetching all bookings.");
		return bookingClient.getAllBookings();
	}

}
