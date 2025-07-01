package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.controllers.BookingController;
import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.BookingService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.ui.Model;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingPageTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @MockBean
    private MockHttpSession session;

    @Mock
    private Model model;

    private User testUser;
    private User actualUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "Doe", "john@example.com", "password123", false, null);
        actualUser = new User(1L, "Kayde", "Page", "kayde@example.com", "password123", false, null);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void logged_in_user_has_no_bookings() throws Exception {
        Collection<Booking> bookings = List.of();

        when(session.getAttribute("user")).thenReturn(testUser);
        when(bookingService.getBookingsUser(testUser.email())).thenReturn(bookings);

        mockMvc.perform(get("/bookings").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("You have no upcoming appointments scheduled.")));
    }

    @Test
    public void logged_in_user_has_bookings() throws Exception {
        Collection<Booking> bookings = List.of(new Booking(
                2L,
                "kayde@example.com",
                "owner2pet1",
                "2024-09-24",
                "13:30",
                "clinic1",
                "vaccination",
                "$25"));

        when(session.getAttribute("user")).thenReturn(actualUser);
        when(bookingService.getBookingsUser(actualUser.email())).thenReturn(bookings);

        mockMvc.perform(get("/bookings").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookings", bookings))
                .andExpect(model().attribute("user", actualUser.email()))
                .andExpect(view().name("bookings/bookings"));
    }

    @Test
    public void user_not_logged_in_gets_redirected_from_booking_page() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/bookings").session(session))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void user_not_logged_in_gets_redirected_from_form_page() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/bookings/newBooking").session(session))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void booking_form_loads() throws Exception {
        when(session.getAttribute("user")).thenReturn(actualUser);

        mockMvc.perform(get("/bookings/newBooking").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("booking", new Booking(null, "", "", "", "", "", ":", null)))
                .andExpect(view().name("bookings/bookingForm"))
                .andExpect(content().string(containsString("Book a Veterinarian Appointment")));
    }

    @Test
    public void test_GetAvailableBookings() throws Exception {
        LocalDate date = LocalDate.now();
        String location = "clinic1";
        List<LocalTime> availableTimes = List.of(
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30));

        when(session.getAttribute("user")).thenReturn(actualUser);

        mockMvc.perform(get("/bookings/newBooking/get-available-times").session(session)
                .param("date", date.toString())
                .param("location", location))
                .andExpect(status().isOk())
                .andExpect(model().attribute("availableTimes", availableTimes))
                .andExpect(model().attribute("selectedDate", date))
                .andExpect(model().attribute("selectedLocation", location))
                .andExpect(view().name("bookings/bookingForm"));
    }

    @Test
    public void test_CreateBooking() throws Exception {
        Booking booking = new Booking(null, testUser.email(), "Sven", "2024-09-24", "10:00", "clinic1", "x-ray",
                "$150");

        when(session.getAttribute("user")).thenReturn(testUser);
         doNothing().when(bookingService).create(null, testUser.email(), booking.petName(),
         booking.date(), booking.time(), booking.location(), booking.visitReason());

        mockMvc.perform(post("/bookings/newBooking/add-booking")
                .session(session)
                        .param("owner", booking.owner())
                .param("petName", booking.petName())
                .param("date", booking.date())
                .param("time", booking.time())
                .param("location", booking.location())
                .param("visitReason", booking.visitReason() + ":" + booking.price()))
                .andExpect(redirectedUrl("/bookings"));
    }

    @Test
    public void CreateBooking_redirects_user_not_logged_in() throws Exception {
        Booking booking = new Booking(null, testUser.email(), "Sven", "2024-09-24", "10:00", "clinic1", "x-ray",
                "$150");

        when(session.getAttribute("user")).thenReturn(null);
        doNothing().when(bookingService).create(null, testUser.email(), booking.petName(),
                booking.date(), booking.time(), booking.location(), booking.visitReason());

        mockMvc.perform(post("/bookings/newBooking/add-booking").session(session).param("owner", booking.owner())
                .param("petName", booking.petName())
                .param("date", booking.date())
                .param("time", booking.time())
                .param("location", booking.location())
                .param("visitReason", booking.visitReason() + ":" + booking.price()))
                .andExpect(redirectedUrl("/"));
    }
}