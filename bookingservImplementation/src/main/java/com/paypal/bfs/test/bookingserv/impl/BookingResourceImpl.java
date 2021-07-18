package com.paypal.bfs.test.bookingserv.impl;

import com.paypal.bfs.test.bookingserv.api.BookingResource;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.service.IBookingService;
import com.paypal.bfs.test.bookingserv.service.mapper.RestToServiceModelConverter;
import com.paypal.bfs.test.bookingserv.service.mapper.ServiceModelToRestModelConverter;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Implementation for Booking Resource API
 *
 */
@Component
@Slf4j
public class BookingResourceImpl implements BookingResource {
	
	@Autowired
	private IBookingService bookingService;
	
	@Autowired
	private RestToServiceModelConverter restToServiceConverter;
	
	@Autowired
	private ServiceModelToRestModelConverter serviceToRestConverter;
	
	
    @Override
    public ResponseEntity<Booking> create(Booking booking) {
    	log.debug("Create booking request recieved.");
    	Booking createdBooking = serviceToRestConverter.toBooking(
    			bookingService.createBooking(restToServiceConverter.toBooking(booking)));
    	
    	return new ResponseEntity<Booking>(createdBooking, HttpStatus.CREATED);
    }


	@Override
	public ResponseEntity<List<Booking>> getAllBookings() {
		log.debug("Get all booking request recieved.");
		return new ResponseEntity<List<Booking>>(
				serviceToRestConverter.toBookingList(
						bookingService.getAllBookings()), 
				HttpStatus.OK);
	}
}
