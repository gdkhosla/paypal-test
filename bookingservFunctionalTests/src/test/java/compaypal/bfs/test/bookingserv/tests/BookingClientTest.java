package compaypal.bfs.test.bookingserv.tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.paypal.bfs.test.bookingserv.dal.client.BookingClient;
import com.paypal.bfs.test.bookingserv.dal.mapper.EntityToServiceMapper;
import com.paypal.bfs.test.bookingserv.dal.mapper.ServiceToEntityMapper;
import com.paypal.bfs.test.bookingserv.entity.Address;
import com.paypal.bfs.test.bookingserv.entity.Booking;
import com.paypal.bfs.test.bookingserv.exception.DataNotFoundException;
import com.paypal.bfs.test.bookingserv.exception.ValidationException;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;
import com.paypal.bfs.test.bookingserv.repository.BookingRepository;

/**
 * Test cases for booking client 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { AppConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
public class BookingClientTest {
	
	@InjectMocks
	private BookingClient bookingClient; 
	
	@Mock
	private BookingRepository bookingRepository;
	
	@Spy
	private ServiceToEntityMapper serviceToEntityMapper;
	
	@Spy
	private EntityToServiceMapper entityToServiceMapper;
	
	@Mock
	private MessageUtil messageUtil;
	
	@Before
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Tests successful booking
	 * @throws ParseException
	 */
	@Test
	public void testSuccessfulBooking() throws ParseException {
		// Preparing booking data
		Booking booking = getExpectedBookingEntity();
		
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer 
		                                                    = getServiceLayerBooking();

		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);
		com.paypal.bfs.test.bookingserv.service.model.Booking createdBooking = bookingClient
				.createBooking(bookingServiceLayer);
		Assert.assertNotNull(createdBooking);
		Assert.assertEquals(createdBooking.getId(), booking.getId());
		assertBookingObject(createdBooking, bookingServiceLayer);
	}
	
	/**
	 * Tests when same booking is tried twice
	 * @throws ParseException
	 */
	@Test(expected = ValidationException.class)
	public void testFailedBooking_DuplicateBookings() throws ParseException {
		// Preparing booking data
		Booking booking = getExpectedBookingEntity();
		
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer 
		                                                    = getServiceLayerBooking();

		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);
		Mockito.when(bookingRepository.findExistingBookings(booking.getFirstName(), 
				booking.getLastName(), booking.getDateOfBirth(), 
				booking.getAddress().getZipCode())).thenReturn(Arrays.asList(booking));
		Mockito.when(messageUtil.getLocalizedMessage(Mockito.anyString())).thenReturn("Test message");
		
		bookingClient.createBooking(bookingServiceLayer);
		
		//Try to persist same booking again
		bookingClient.createBooking(bookingServiceLayer);
		
	}
	
	/**
	 * Tests where another booking with overlapped period is tried
	 * @throws ParseException
	 */
	@Test(expected = ValidationException.class)
	public void testFailedBooking_OverlappedBookings1() throws ParseException {
		// Preparing booking data
		Booking booking = getExpectedBookingEntity();
		
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer 
		                                                    = getServiceLayerBooking();

		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);
		
		bookingClient.createBooking(bookingServiceLayer);
		
		Mockito.when(bookingRepository.findExistingBookings(booking.getFirstName(), 
				booking.getLastName(), booking.getDateOfBirth(), 
				booking.getAddress().getZipCode())).thenReturn(Arrays.asList(booking));
		Mockito.when(messageUtil.getLocalizedMessage(Mockito.anyString())).thenReturn("Test message");
		
		//Try to persist same booking again
		bookingServiceLayer.setCheckIn(bookingServiceLayer.getCheckIn()+2);
		bookingClient.createBooking(bookingServiceLayer);
		
	}
	
	/**
	 * Tests where another booking with overlapped period is tried
	 * @throws ParseException
	 */
	@Test(expected = ValidationException.class)
	public void testFailedBooking_OverlappedBookings2() throws ParseException {
		// Preparing booking data
		Booking booking = getExpectedBookingEntity();
		
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer 
		                                                    = getServiceLayerBooking();

		Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);
		
		bookingClient.createBooking(bookingServiceLayer);
		
		Mockito.when(bookingRepository.findExistingBookings(booking.getFirstName(), 
				booking.getLastName(), booking.getDateOfBirth(), 
				booking.getAddress().getZipCode())).thenReturn(Arrays.asList(booking));
		Mockito.when(messageUtil.getLocalizedMessage(Mockito.anyString())).thenReturn("Test message");
		//Try to persist same booking again
		bookingServiceLayer.setCheckOut(bookingServiceLayer.getCheckOut()-2);
		bookingClient.createBooking(bookingServiceLayer);
		
	}
	
	/**
	 * Tests successfully Get bookings
	 * @throws ParseException
	 */
	@Test
	public void testSuccessfulGetBookings() throws ParseException {
		// Preparing booking data
		Booking booking1 = getExpectedBookingEntity();
		booking1.setId(1);
		Booking booking2 = getExpectedBookingEntity();
		booking2.setId(2);
		List<Booking> persistedBookings = new ArrayList<>();
		persistedBookings.add(booking1);
		persistedBookings.add(booking2);
		
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer1 
		                                                    = getServiceLayerBooking();
		bookingServiceLayer1.setId(1);
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer2 
        = getServiceLayerBooking();
		bookingServiceLayer2.setId(2);

		Mockito.when(bookingRepository.findAll()).thenReturn(persistedBookings);
		List<com.paypal.bfs.test.bookingserv.service.model.Booking> createdBookings = bookingClient
				.getAllBookings();
		Assert.assertNotNull(createdBookings);
		Assert.assertEquals(2, createdBookings.size());
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingWithId1 = null;
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingWithId2 = null;
		for(com.paypal.bfs.test.bookingserv.service.model.Booking createdBooking : createdBookings) {
			if(createdBooking.getId() == 1) {
				bookingWithId1 = createdBooking;
			}
			if(createdBooking.getId() == 2) {
				bookingWithId2 = createdBooking;
			}
		}
		Assert.assertNotNull(bookingWithId1);
		Assert.assertNotNull(bookingWithId2);
		Assert.assertEquals(new Integer(1), bookingWithId1.getId());
		Assert.assertEquals(new Integer(2), bookingWithId2.getId());
		assertBookingObject(bookingWithId1, bookingServiceLayer1);
		assertBookingObject(bookingWithId2, bookingServiceLayer2);
	}
	
	
	/**
	 * Tests failed get bookings with no contents
	 */
	@Test(expected = DataNotFoundException.class)
	public void testFailedGetBookings_NoContent() {
		bookingClient.getAllBookings();
	}
	
	
	private Booking getExpectedBookingEntity() throws ParseException {
		// Preparing booking data
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
		booking.setCheckIn(checkinDate.getTime());

		String checkoutString = "July 19, 2021";
		Date checkoutDate = format.parse(checkoutString);
		booking.setCheckOut(checkoutDate.getTime());

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
		booking.setId(1);
		return booking;
	}
	
	private com.paypal.bfs.test.bookingserv.service.model.Booking getServiceLayerBooking() throws ParseException {
		com.paypal.bfs.test.bookingserv.service.model.Booking bookingServiceLayer = new com.paypal.bfs.test.bookingserv.service.model.Booking();
		bookingServiceLayer.setFirstName("Test First Name");
		bookingServiceLayer.setLastName("Test Last Name");

		// Setting date fields
		DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		String dobString = "January 2, 1986";
		Date dob = format.parse(dobString);
		bookingServiceLayer.setDateOfBirth(dob.getTime());

		String checkinString = "July 17, 2021";
		Date checkinDate = format.parse(checkinString);
		bookingServiceLayer.setCheckIn(checkinDate.getTime());

		String checkoutString = "July 19, 2021";
		Date checkoutDate = format.parse(checkoutString);
		bookingServiceLayer.setCheckOut(checkoutDate.getTime());

		bookingServiceLayer.setDeposit(2000.00);
		bookingServiceLayer.setTotalPrice(5000.00);

		// Setting address
		com.paypal.bfs.test.bookingserv.service.model.Address addressServiceLayer = new com.paypal.bfs.test.bookingserv.service.model.Address();
		addressServiceLayer.setLine1("address  line 1");
		addressServiceLayer.setLine2("Address line 2");
		addressServiceLayer.setCity("Address city");
		addressServiceLayer.setState("Address state");
		addressServiceLayer.setCountry("Address country");
		addressServiceLayer.setZipCode("Address zip code");
		bookingServiceLayer.setAddress(addressServiceLayer);
		return bookingServiceLayer;
	}
	
	private void assertBookingObject(com.paypal.bfs.test.bookingserv.service.model.Booking createdBooking
			, com.paypal.bfs.test.bookingserv.service.model.Booking requestedBooking) {
		Assert.assertNotNull(createdBooking);
		Assert.assertEquals(createdBooking.getFirstName(), requestedBooking.getFirstName());
		Assert.assertEquals(createdBooking.getLastName(), requestedBooking.getLastName());
		Assert.assertEquals(createdBooking.getCheckIn(), requestedBooking.getCheckIn());
		Assert.assertEquals(createdBooking.getCheckOut(), requestedBooking.getCheckOut());
		Assert.assertEquals(createdBooking.getDateOfBirth(), requestedBooking.getDateOfBirth());
		Assert.assertEquals(createdBooking.getDeposit(), requestedBooking.getDeposit());
		Assert.assertEquals(createdBooking.getTotalPrice(), requestedBooking.getTotalPrice());
		com.paypal.bfs.test.bookingserv.service.model.Address createdAddress = createdBooking.getAddress();
		com.paypal.bfs.test.bookingserv.service.model.Address requestedAddress = requestedBooking.getAddress();
		Assert.assertEquals(createdAddress.getLine1(), requestedAddress.getLine1());
		Assert.assertEquals(createdAddress.getLine2(), requestedAddress.getLine2());
		Assert.assertEquals(createdAddress.getCity(), requestedAddress.getCity());
		Assert.assertEquals(createdAddress.getState(), requestedAddress.getState());
		Assert.assertEquals(createdAddress.getCountry(), requestedAddress.getCountry());
		Assert.assertEquals(createdAddress.getZipCode(), requestedAddress.getZipCode());
	}

}
