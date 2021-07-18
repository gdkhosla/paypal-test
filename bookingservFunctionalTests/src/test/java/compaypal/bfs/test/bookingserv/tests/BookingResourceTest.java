package compaypal.bfs.test.bookingserv.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.controller.adviser.ApplicationExceptionHandlerAdvice;
import com.paypal.bfs.test.bookingserv.controller.adviser.ErrorResponse;
import com.paypal.bfs.test.bookingserv.exception.DataNotFoundException;
import com.paypal.bfs.test.bookingserv.exception.ValidationException;
import com.paypal.bfs.test.bookingserv.impl.BookingResourceImpl;
import com.paypal.bfs.test.bookingserv.service.BookingService;
import com.paypal.bfs.test.bookingserv.service.mapper.RestToServiceModelConverter;
import com.paypal.bfs.test.bookingserv.service.mapper.ServiceModelToRestModelConverter;

/** Test cases for booking scenarios */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BookingResourceTest {

	@InjectMocks
	private BookingResourceImpl bookingResource;

	private MockMvc mvc;

	@Spy
	private RestToServiceModelConverter restToServiceModelConverter = new RestToServiceModelConverter();

	@Spy
	private ServiceModelToRestModelConverter serviceModelToRestModelConverter = new ServiceModelToRestModelConverter();

	@Mock
	private BookingService bookingService;

	private ObjectMapper mapper = new ObjectMapper();

	// Service layer response objects

	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(bookingResource)
				.setControllerAdvice(new ApplicationExceptionHandlerAdvice()).build();
	}

	/**
	 * Test successful booking request resposne
	 * @throws Exception
	 */
	@Test
	public void testSuccessfulBooking() throws Exception {
		// Preparing booking data
		Booking booking = prepareBookingRequest();

		// Mock the service layer response
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer = restToServiceModelConverter
				.toBooking(booking);
		bookingServiceLayer.setId(1);
		Mockito.when(bookingService.createBooking(Mockito.any())).thenReturn(bookingServiceLayer);

		MvcResult result = mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(booking))).andExpect(status().isCreated()).andReturn();
		Assert.assertNotNull(result);
		String response = result.getResponse().getContentAsString();
		Booking createdBooking = mapper.readValue(response, Booking.class);
		Assert.assertEquals(createdBooking.getId(), new Integer(1));
		compareRestResponse(createdBooking, booking);
	}
	
	/**
	 * Tests ValidationException scenario
	 * @throws Exception
	 */
	@Test
	public void testFailedBooking_ValidationFailed() throws Exception {
		// Preparing booking data
		Booking booking = prepareBookingRequest();

		Mockito.when(bookingService.createBooking(Mockito.any())).thenThrow(ValidationException.class);

		MvcResult result = mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(booking))).andExpect(status().isBadRequest()).andReturn();
		Assert.assertNotNull(result);
		String response = result.getResponse().getContentAsString();
		ErrorResponse errResponse = mapper.readValue(response, ErrorResponse.class);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, errResponse.getStatusCode());
		
	}
	
	/**
	 * Tests IllegalArgumentException scenario
	 * @throws Exception
	 */
	@Test
	public void testBooking_IllegalArgs() throws Exception {
		// Preparing booking data
		Booking booking = prepareBookingRequest();

		Mockito.when(bookingService.createBooking(Mockito.any())).thenThrow(IllegalArgumentException.class);

		MvcResult result = mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(booking))).andExpect(status().isBadRequest()).andReturn();
		Assert.assertNotNull(result);
		String response = result.getResponse().getContentAsString();
		ErrorResponse errResponse = mapper.readValue(response, ErrorResponse.class);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, errResponse.getStatusCode());
		
	}
	
	/**
	 * Tests successful get booking
	 * @throws Exception
	 */
	@Test
	public void testGetBookingSuccesfully() throws Exception {
		// Preparing booking data
		Booking booking1 = prepareBookingRequest();
		Booking booking2 = prepareBookingRequest();

		// Mock the service layer response
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer1 = restToServiceModelConverter
				.toBooking(booking1);
		bookingServiceLayer1.setId(1);
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer2 = restToServiceModelConverter
				.toBooking(booking2);
		bookingServiceLayer2.setId(2);
		List<com.paypal.bfs.test.bookingserv.service.model.Booking> bookingServiceLayerList = new ArrayList<>();
		bookingServiceLayerList.add(bookingServiceLayer1);
		bookingServiceLayerList.add(bookingServiceLayer2);
		
		Mockito.when(bookingService.getAllBookings()).thenReturn(bookingServiceLayerList);

		MvcResult result =
                mvc.perform(get("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
		String response = result.getResponse().getContentAsString();
		Booking[] createdBookings = mapper.readValue(response, Booking[].class);
		Assert.assertEquals(2, createdBookings.length);
		Booking createdBookingWithId_1 = null;
		Booking createdBookingWithId_2 = null;
		for(Booking createdBooking : createdBookings) {
			if(createdBooking.getId().equals(1)) {
				createdBookingWithId_1 = createdBooking;
			}
			if(createdBooking.getId().equals(2)) {
				createdBookingWithId_2 = createdBooking;
			}
		}
		Assert.assertNotNull(createdBookingWithId_1);
		Assert.assertNotNull(createdBookingWithId_2);
		compareRestResponse(booking1, createdBookingWithId_1);
		compareRestResponse(booking2, createdBookingWithId_2);
	}
	
	/**
	 * Tests successful get booking
	 * @throws Exception
	 */
	@Test
	public void testFailedGetBooking_NoDataFound() throws Exception {
		// Mock the service layer response
		Mockito.when(bookingService.getAllBookings()).thenThrow(DataNotFoundException.class);

		//Assess the return status code must be 204 - No Content 
		mvc.perform(get("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();
	}
	
	
	private Booking prepareBookingRequest() throws ParseException {
		Booking booking = new Booking();
		booking.setFirstName("Test First Name");
		booking.setLastName("Test Last Name");

		// Setting date fields
		DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		String dobString = "January 2, 1986";
		Date dob = format.parse(dobString);
		booking.setDateOfBirth(dob.getTime());

		String checkinString = "July 17, 2021";
		Date checkinDate = format.parse(checkinString);
		booking.setCheckin(checkinDate.getTime());

		String checkoutString = "July 19, 2021";
		Date checkoutDate = format.parse(checkoutString);
		booking.setCheckin(checkoutDate.getTime());

		booking.setDeposit(2000.00);
		booking.setTotalPrice(5000.00);

		// Setting address
		Address address = new Address();
		address.setLine1("address  line 1");
		address.setLine2("Address line 2");
		address.setCity("Address city");
		address.setState("Address state");
		address.setCountry("Address country");
		address.setZipCode("Address zip code");
		booking.setAddress(address);
		return booking;
	}

	private void compareRestResponse(Booking createdBooking, Booking requestedBooking) {
		Assert.assertNotNull(createdBooking);
		Assert.assertEquals(createdBooking.getFirstName(), requestedBooking.getFirstName());
		Assert.assertEquals(createdBooking.getLastName(), requestedBooking.getLastName());
		Assert.assertEquals(createdBooking.getCheckin(), requestedBooking.getCheckin());
		Assert.assertEquals(createdBooking.getCheckout(), requestedBooking.getCheckout());
		Assert.assertEquals(createdBooking.getDateOfBirth(), requestedBooking.getDateOfBirth());
		Assert.assertEquals(createdBooking.getDeposit(), requestedBooking.getDeposit());
		Assert.assertEquals(createdBooking.getTotalPrice(), requestedBooking.getTotalPrice());
		Address createdAddress = createdBooking.getAddress();
		Address requestedAddress = requestedBooking.getAddress();
		Assert.assertEquals(createdAddress.getLine1(), requestedAddress.getLine1());
		Assert.assertEquals(createdAddress.getLine2(), requestedAddress.getLine2());
		Assert.assertEquals(createdAddress.getCity(), requestedAddress.getCity());
		Assert.assertEquals(createdAddress.getState(), requestedAddress.getState());
		Assert.assertEquals(createdAddress.getCountry(), requestedAddress.getCountry());
		Assert.assertEquals(createdAddress.getZipCode(), requestedAddress.getZipCode());
	}
}
