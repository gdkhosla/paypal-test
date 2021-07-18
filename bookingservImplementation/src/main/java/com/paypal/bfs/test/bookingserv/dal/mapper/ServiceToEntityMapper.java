package com.paypal.bfs.test.bookingserv.dal.mapper;

import org.springframework.stereotype.Component;

import com.paypal.bfs.test.bookingserv.entity.Address;
import com.paypal.bfs.test.bookingserv.entity.Booking;

/**
 * Converts service layer model objects to entity objects
 *
 */
@Component
public class ServiceToEntityMapper {
	
	/**
	 * Converts address service layer object to entity object
	 * @param address - Service layer address object
	 * @return Address entity object
	 */
	public Address toAddressEntity(com.paypal.bfs.test.bookingserv.service.model.Address address) {
		if(address == null) {
			throw new IllegalArgumentException("Address cannot be null");
		}
		return Address.builder()
				               .line1(address.getLine1())
				               .line2(address.getLine2())
				               .city(address.getCity())
				               .country(address.getCountry())
				               .state(address.getState())
				               .zipCode(address.getZipCode())
				               .build();
	}
	
	/**
	 * Converts Booking service layer object to entity object
	 * @param booking - Service layer Booking object
	 * @return - Booking entity
	 */
	public Booking toBookingEntity(com.paypal.bfs.test.bookingserv.service.model.Booking booking) {
		if(booking == null) {
			throw new IllegalArgumentException("Booking cannot be null");
		}
		
		return Booking.builder()
				.firstName(booking.getFirstName())
				.lastName(booking.getLastName())
				.checkIn(booking.getCheckIn())
				.checkOut(booking.getCheckOut())
				.totalPrice(booking.getTotalPrice())
				.deposit(booking.getDeposit())
				.dateOfBirth(booking.getDateOfBirth())
				.address(toAddressEntity(booking.getAddress()))
				.build();
	}

}
