package compaypal.bfs.test.bookingserv.tests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.paypal.bfs.test.bookingserv.exception.DataNotFoundException;
import com.paypal.bfs.test.bookingserv.exception.ValidationException;
import com.paypal.bfs.test.bookingserv.i18n.MessageUtil;
import com.paypal.bfs.test.bookingserv.service.BookingService;
import com.paypal.bfs.test.bookingserv.service.model.Address;
import com.paypal.bfs.test.bookingserv.service.model.Booking;
import com.paypal.bfs.test.bookingserv.validate.BookingRequestValidator;

/**
 * Test cases for booking service 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = { AppConfig.class }, 
  loader = AnnotationConfigContextLoader.class)
public class BookingServiceTest {
	
	@InjectMocks
	private BookingService bookingService;
	
	@Mock
	private BookingClient bookingClient;
	
	@Spy
	private ServiceToEntityMapper serviceToEntityMapper = new ServiceToEntityMapper();
	
	@Spy
	private BookingRequestValidator bookingRequestValidator;
	
	@Mock
	private MessageUtil messageUtil;
	
	@Spy
	private EntityToServiceMapper entityToServiceMapper = new EntityToServiceMapper();
	
	@Before
	public void beforeTest() {
		messageUtil = mock(MessageUtil.class);
		bookingRequestValidator = spy(new BookingRequestValidator(messageUtil));
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Tests succesful booking
	 * @throws ParseException
	 */
	@Test
	public void testSuccessfulBooking() throws ParseException {
		// Preparing booking data
		Booking booking = prepareBooking();
		
		Mockito.when(bookingClient.createBooking(Mockito.any())).thenReturn(booking);
		
		Booking createdBooking = bookingService.createBooking(booking);
		Assert.assertNotNull(createdBooking.getId());
		assertBookingObject(createdBooking, booking);
	}
	
	/**
	 * Tests line2 is not mandatory
	 * @throws ParseException
	 */
	@Test
	public void testSuccessfulBooking_line2AddressNotProvided() throws ParseException {
		// Preparing booking data
		Booking booking = prepareBooking();
		
		Mockito.when(bookingClient.createBooking(Mockito.any())).thenReturn(booking);
		
		Booking createdBooking = bookingService.createBooking(booking);
		Assert.assertNotNull(createdBooking.getId());
		assertBookingObject(createdBooking, booking);
	}
	
	/**
	 * Tests excpetion when a required field is not provided
	 * @throws ParseException
	 */
	@Test(expected = ValidationException.class)
	public void testFailedBooking_addressNotProvided() throws ParseException {
		// Preparing booking data
		Booking booking = prepareBooking();
		booking.setAddress(null);
		
		Mockito.when(bookingClient.createBooking(Mockito.any())).thenReturn(booking);
		Mockito.when(messageUtil.getLocalizedMessage(Mockito.anyString())).thenReturn("Test message");
		
		bookingService.createBooking(booking);
	}
	
	/**
	 * Tests get bookings
	 * @throws ParseException
	 */
	@Test
	public void testGetBookingSuccesfully() throws ParseException {
		// Preparing booking data
		Booking booking1 = prepareBooking();
		booking1.setId(1);
		Booking booking2 = prepareBooking();
		booking2.setId(2);
		List<Booking> bookings = new ArrayList<>();
		bookings.add(booking1);
		bookings.add(booking2);
		
		Mockito.when(bookingClient.getAllBookings()).thenReturn(bookings);
		
		List<Booking> persistedBookings = bookingService.getAllBookings();
		
		Assert.assertNotNull(persistedBookings);
		Assert.assertEquals(2, persistedBookings.size());
		Booking persistedBookingWithID1 = null;
		Booking persistedBookingWithID2 = null;
		for(Booking persistedBooking : persistedBookings) {
			if(persistedBooking.getId() == 1) {
				persistedBookingWithID1 = persistedBooking;
			}
			if(persistedBooking.getId() == 2) {
				persistedBookingWithID2 = persistedBooking;
			}
		}
		Assert.assertNotNull(persistedBookingWithID1);
		Assert.assertNotNull(persistedBookingWithID2);
		assertBookingObject(booking1, persistedBookingWithID1);
		assertBookingObject(booking2, persistedBookingWithID2);
		
	}
	
	/**
	 * Tests get bookings failed scenario when  there is no content
	 * @throws ParseException
	 */
	@Test(expected = DataNotFoundException.class)
	public void testGetBookingFailed_NoContent() throws ParseException {
		
		Mockito.when(bookingClient.getAllBookings()).thenThrow(DataNotFoundException.class);
		
		bookingService.getAllBookings();
		
	}
	
	private Booking prepareBooking() throws ParseException {
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
	
	private void assertBookingObject(Booking createdBooking, Booking requestedBooking) {
		Assert.assertNotNull(createdBooking);
		Assert.assertEquals(createdBooking.getFirstName(), requestedBooking.getFirstName());
		Assert.assertEquals(createdBooking.getLastName(), requestedBooking.getLastName());
		Assert.assertEquals(createdBooking.getCheckIn(), requestedBooking.getCheckIn());
		Assert.assertEquals(createdBooking.getCheckOut(), requestedBooking.getCheckOut());
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
