package com.paypal.bfs.test.bookingserv.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.paypal.bfs.test.bookingserv.exception.ValidationException;
import com.paypal.bfs.test.bookingserv.i18n.Constants;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;
import com.paypal.bfs.test.bookingserv.service.model.Address;
import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * Validator for booking request
 *
 */
@Component
public class BookingRequestValidator {
	
	@Autowired
	private MessageUtil messageUtil;
	
	public BookingRequestValidator(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}
	
	public void validateBookingRequest(Booking booking) {
		StringBuilder messageBuilder = new StringBuilder();
		String bookingErrorMessages = getBookingErrorMessages(booking);
		if(!StringUtils.isEmpty(bookingErrorMessages)) {
			messageBuilder.append(bookingErrorMessages);
		}
		String addressErrorMessages = null;
		if(booking.getAddress() != null) {
			addressErrorMessages = getAddressErrorMessages(booking.getAddress());
		}
		if(!StringUtils.isEmpty(addressErrorMessages)) {
			messageBuilder.append(addressErrorMessages);
		}
		if(messageBuilder.length()>0) {
			throw new ValidationException(messageBuilder.toString());
		}
	}
	
    private String getBookingErrorMessages(Booking booking) {
    	if(booking == null) {
    		throw new ValidationException(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_CANNOT_BE_NULL));
    	}
    	
    	StringBuilder messageBuilder = new StringBuilder();
    	if(StringUtils.isEmpty(booking.getFirstName())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_FIRST_NAME_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(StringUtils.isEmpty(booking.getLastName())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_LAST_NAME_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getTotalPrice() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_TOTAL_PRICE_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getDeposit() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_DEPOSIT_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getDateOfBirth() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_DOB_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getCheckIn() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_CHECKIN_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getCheckOut() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_CHECKOUT_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(booking.getAddress() == null) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.BOOKING_ADDRESS_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	
    	return messageBuilder.toString();
    }
    
    private String getAddressErrorMessages(Address address) {
    	if(address == null) {
    		throw new ValidationException(messageUtil.getLocalizedMessage(Constants.ADDRESS_CANNOT_BE_NULL));
    	}
    	StringBuilder messageBuilder = new StringBuilder();
    	if(StringUtils.isEmpty(address.getLine1())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.ADDRESS_LINE1_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(StringUtils.isEmpty(address.getCity())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.ADDRESS_CITY_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(StringUtils.isEmpty(address.getState())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.ADDRESS_STATE_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(StringUtils.isEmpty(address.getCountry())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.ADDRESS_COUNTRY_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	if(StringUtils.isEmpty(address.getZipCode())) {
    		messageBuilder.append(messageUtil
    				.getLocalizedMessage(Constants.ADDRESS_ZIP_CODE_REQUIRED))
    		.append(System.lineSeparator());
    	}
    	return messageBuilder.toString();
    }
}
