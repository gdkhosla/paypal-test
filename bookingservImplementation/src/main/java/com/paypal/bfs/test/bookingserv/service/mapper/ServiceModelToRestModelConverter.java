package com.paypal.bfs.test.bookingserv.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.i18n.Constants;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;

/**
 * Converts Service layer models to REST layer models
 *
 */
@Component
public class ServiceModelToRestModelConverter {
	
	@Autowired
	private MessageUtil messageUtil;
	
	/**
	 * Converts service layer Address object to REST model
	 * @param address Service layer Address object
	 * @return REST model Address
	 */
	public Address toAddress(com.paypal.bfs.test.bookingserv.service.model.Address address) {
		if(address == null) {
    		throw new IllegalArgumentException(messageUtil.getLocalizedMessage(
    				Constants.ADDRESS_CANNOT_BE_NULL));
    	}
		Address addressRestObject = new Address();
		addressRestObject.setLine1(address.getLine1());
		addressRestObject.setLine2(address.getLine2());
		addressRestObject.setCity(address.getCity());
		addressRestObject.setState(address.getState());
		addressRestObject.setCountry(address.getCountry());
		addressRestObject.setZipCode(address.getZipCode());
		return addressRestObject;
	}
	
	/**
	 * Converts service layer Booking object to REST object
	 * @param booking Service layer Booking object
	 * @return REST model Booking
	 */
	public Booking toBooking(com.paypal.bfs.test.bookingserv.service.model.Booking booking) {
    	if(booking == null) {
    		throw new IllegalArgumentException(messageUtil.getLocalizedMessage(
    				Constants.BOOKING_CANNOT_BE_NULL));
    	}
    	Booking bookingRestObject = new Booking();
    	bookingRestObject.setFirstName(booking.getFirstName());
    	bookingRestObject.setLastName(booking.getLastName());
    	bookingRestObject.setDateOfBirth(booking.getDateOfBirth());
    	bookingRestObject.setAddress(toAddress(booking.getAddress()));
    	bookingRestObject.setTotalPrice(booking.getTotalPrice());
    	bookingRestObject.setDeposit(booking.getDeposit());
    	bookingRestObject.setCheckin(booking.getCheckIn());
    	bookingRestObject.setCheckout(booking.getCheckOut());
    	bookingRestObject.setId(booking.getId());
    	return bookingRestObject;
    }
	
	/**
	 * Converts service layer Booking list to REST layer booking list
	 * @param bookingList - Booking list of service layer
	 * @return Booking list of REST layer
	 */
	public List<Booking> toBookingList(List<com.paypal.bfs.test.bookingserv.service.model.Booking> bookingList){
		if(CollectionUtils.isEmpty(bookingList)) {
			throw new IllegalArgumentException(messageUtil.getLocalizedMessage(Constants.BOOKING_CANNOT_BE_NULL));
		}
		return bookingList.stream().map(this::toBooking).collect(Collectors.toList());
	}

}
