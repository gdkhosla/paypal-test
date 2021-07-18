package com.paypal.bfs.test.bookingserv.dal.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.paypal.bfs.test.bookingserv.service.model.Address;
import com.paypal.bfs.test.bookingserv.service.model.Booking;

/**
 * Converts entity objects to service layer objects
 *
 */
@Component
public class EntityToServiceMapper {

	/**
	 * Converts address entity object to service model object
	 * @param address - address entity
	 * @return Address service layer object
	 */
	public Address toAddress(com.paypal.bfs.test.bookingserv.entity.Address address) {
		if(address == null) {
			throw new IllegalArgumentException("Address cannot be null.");
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
	 * Converts booking entity to booking service model
	 * @param booking -  booking entity
	 * @return Booking service layer object
	 */
	public Booking toBooking(com.paypal.bfs.test.bookingserv.entity.Booking booking) {
		if(booking == null) {
			throw new IllegalArgumentException("Booking cannot be null");
		}
		return Booking.builder()
				.firstName(booking.getFirstName())
				.lastName(booking.getLastName())
				.checkIn(booking.getCheckIn())
				.checkOut(booking.getCheckOut())
				.dateOfBirth(booking.getDateOfBirth())
				.address(toAddress(booking.getAddress()))
				.deposit(booking.getDeposit())
				.totalPrice(booking.getTotalPrice())
				.id(booking.getId())
				.build();
	}
	
	/**
	 * Converts list of booking entity objects to service layer booking objects' list
	 * @param bookingList - Booking entity object's list
	 * @return Booking service layer object's list
	 */
	public List<Booking> toBookingList(List<com.paypal.bfs.test.bookingserv.entity.Booking> bookingList){
		if(CollectionUtils.isEmpty(bookingList)) {
			throw new IllegalArgumentException("Booking list cannot be null.");
		}
		List<Booking> bookingServiceObjectList = 
				bookingList.stream().map(this::toBooking).collect(Collectors.toList());
		return bookingServiceObjectList;
	}
}
