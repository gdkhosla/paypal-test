package com.paypal.bfs.test.bookingserv.api;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.bfs.test.bookingserv.api.model.Booking;

@RestController
public interface BookingResource {
    /**
     * Create {@link Booking} resource
     *
     * @param booking the booking object
     * @return the created booking
     */
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON,
            method = RequestMethod.POST,
            value = "/v1/bfs/booking")
    ResponseEntity<Booking> create(@RequestBody Booking booking);

    /**
     * Gets all {@link Booking} resource
     *
     * @return all booking records
     */
    @RequestMapping(
            produces = MediaType.APPLICATION_JSON,
            consumes = MediaType.APPLICATION_JSON,
            method = RequestMethod.GET,
            value = "/v1/bfs/booking")
    ResponseEntity<List<Booking>> getAllBookings();
}
