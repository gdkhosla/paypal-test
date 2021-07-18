package com.paypal.bfs.test.bookingserv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.paypal.bfs.test.bookingserv.entity.Booking;

/**
 * Repository for booking entity
 *
 */
public interface BookingRepository extends JpaRepository<Booking, String> {
	
	@Query("Select b from Booking b where b.firstName = :firstName and b.lastName = :lastName"
			+ " and b.dateOfBirth =:dateOfBirth and b.address.zipCode = :zipCode")
	List<Booking> findExistingBookings(@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("dateOfBirth") Long dateOfBirth,
			@Param("zipCode") String zipCode);

}
