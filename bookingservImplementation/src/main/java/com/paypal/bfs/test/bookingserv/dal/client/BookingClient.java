package com.paypal.bfs.test.bookingserv.dal.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.paypal.bfs.test.bookingserv.dal.mapper.EntityToServiceMapper;
import com.paypal.bfs.test.bookingserv.dal.mapper.ServiceToEntityMapper;
import com.paypal.bfs.test.bookingserv.exception.DataNotFoundException;
import com.paypal.bfs.test.bookingserv.exception.ValidationException;
import com.paypal.bfs.test.bookingserv.i18n.Constants;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;
import com.paypal.bfs.test.bookingserv.repository.BookingRepository;
import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * Client implementation for booking operations
 *
 */
@Component
public class BookingClient implements IBookingClient {
	
	@Autowired
	private ServiceToEntityMapper serviceToEntityMapper;
	
	@Autowired
	private EntityToServiceMapper entityToServiceMapper;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private MessageUtil messageUtil;

	@Override
	public Booking createBooking(Booking booking) {
		if(booking == null) {
			throw new IllegalArgumentException(messageUtil.getLocalizedMessage(
					Constants.BOOKING_CANNOT_BE_NULL));
		}
		//Check for existing requests
        List<com.paypal.bfs.test.bookingserv.entity.Booking> existingBookings = bookingRepository
        		.findExistingBookings(booking.getFirstName(), booking.getLastName(), 
        				booking.getDateOfBirth(), booking.getAddress().getZipCode());
        
        if(!CollectionUtils.isEmpty(existingBookings)) {
        	for(com.paypal.bfs.test.bookingserv.entity.Booking existingBooking : existingBookings) {
        		//Check for existing booking with matching check in check out
        		if(existingBooking.getCheckIn().equals(booking.getCheckIn()) && existingBooking.getCheckOut().equals(booking.getCheckOut())) {
        			String[] messageParams  = new String[2];
        			messageParams[0] = existingBooking.getCheckIn().toString();
        			messageParams[1] = existingBooking.getCheckOut().toString();
        			throw new ValidationException(messageUtil.getLocalizedMessageWithParameters(Constants.BOOKING_ALREADY_EXISTING, messageParams));
        		}
        		//Check for existing booking with overlapping check in check out
        		if((existingBooking.getCheckIn() <= booking.getCheckIn()  &&  existingBooking.getCheckOut()>=booking.getCheckIn())
        				||  (existingBooking.getCheckIn()<=booking.getCheckOut() &&  existingBooking.getCheckOut()>= booking.getCheckOut())) {
        			String[] messageParams  = new String[5];
        			messageParams[0] = booking.getCheckIn().toString();
        			messageParams[1] = booking.getCheckOut().toString();
        			messageParams[2] = existingBooking.getId().toString();
        			messageParams[3] = existingBooking.getCheckIn().toString();
        			messageParams[4] = existingBooking.getCheckOut().toString();
        			throw new ValidationException(messageUtil.getLocalizedMessageWithParameters(Constants.BOOKING_OVERLAPS_WITH_EXISTING, messageParams));
        		}
        	}
        }
        
		com.paypal.bfs.test.bookingserv.entity.Booking persistedRecord = bookingRepository.save(
				serviceToEntityMapper.toBookingEntity(booking));
		
		return entityToServiceMapper.toBooking(persistedRecord);
	}

	@Override
	public List<Booking> getAllBookings() {
		
		List<com.paypal.bfs.test.bookingserv.entity.Booking> bookingRecords 
		= bookingRepository.findAll();
		
		if(CollectionUtils.isEmpty(bookingRecords)) {
			throw new DataNotFoundException(messageUtil.getLocalizedMessage(
					Constants.DATA_NOT_FOUND));
		}
		return entityToServiceMapper.toBookingList(bookingRecords);
	}

}
