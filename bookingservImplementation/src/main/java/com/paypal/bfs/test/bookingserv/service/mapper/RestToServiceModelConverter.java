package com.paypal.bfs.test.bookingserv.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paypal.bfs.test.bookingserv.i18n.Constants;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;
import com.paypal.bfs.test.bookingserv.service.model.Address;
import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * Rest model to service model converter
 *
 */
@Component
public class RestToServiceModelConverter {
	
	@Autowired
	private MessageUtil messageUtil;
	
    /**
     * Converts REST model Address to service layer model Address
     * @param address  - REST model Address
     * @return Service layer model Address
     */
    public Address toAddress(com.paypal.bfs.test.bookingserv.api.model.Address address) {
    	if(address == null) {
    		throw new IllegalArgumentException(messageUtil.getLocalizedMessage(
    				Constants.ADDRESS_CANNOT_BE_NULL));
    	}
    	return Address.builder()
    			.line1(address.getLine1())
    			.line2(address.getLine2())
    			.city(address.getCity())
    			.state(address.getState())
    			.country(address.getCountry())
    			.zipCode(address.getZipCode())
    			.build();
    }
    
    /**
     * Converts REST model Booking to service layer model Booking
     * @param booking REST model Booking
     * @return Service layer model Booking
     */
    public Booking toBooking(com.paypal.bfs.test.bookingserv.api.model.Booking booking) {
    	if(booking == null) {
    		throw new IllegalArgumentException(messageUtil.getLocalizedMessage(
    				Constants.BOOKING_CANNOT_BE_NULL));
    	}
    	return Booking.builder()
    			.firstName(booking.getFirstName())
    			.lastName(booking.getLastName())
    			.dateOfBirth(booking.getDateOfBirth())
    			.address(toAddress(booking.getAddress()))
    			.totalPrice(booking.getTotalPrice())
    			.deposit(booking.getDeposit())
    			.checkIn(booking.getCheckin())
    			.checkOut(booking.getCheckout())
    			.build();
    }
}
