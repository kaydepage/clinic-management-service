package au.edu.rmit.sept.webapp.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.services.BookingService;
import au.edu.rmit.sept.webapp.services.BookingServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @MockBean
    private BookingService service;
    @Mock
    private BookingRepository repo;

    @BeforeEach
    public void setUp() {
        repo = new BookingRepositoryImpl(source);

        service = new BookingServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS bookings");
        // Repo will contain two hard-coded bookings created in BookingServiceImpl
        service.initaliseDB();
    }

    @AfterEach
    public void tearDown() {
        // Remove any new bookings made in tests
        source.execute("DROP TABLE IF EXISTS bookings");
    }

    @Test
    void findAll_should_return_bookings() throws Exception {
        var bookings = repo.findAll();
        assertEquals(2, bookings.size());
    }

    @Test
    void insertBooking_should_update_bookings_table() throws Exception {

        // id = 3
        repo.insertBooking(
                "Jeff",
                "Max",
                "2024-11-12",
                "14:00",
                "clinic2",
                "Vaccination",
                "$25");

        // id = 4
        repo.insertBooking(
                "Anabelle",
                "Sparkles",
                "2024-05-02",
                "16:30",
                "clinic2",
                "Check-up",
                "$80");

        var bookings = repo.findAll();
        assertEquals(4, bookings.size());
    }

    @Test
    void deleteBooking_should_update_bookings_table() throws Exception {

        repo.deleteById(2L);
        var bookings = repo.findAll();
        assertEquals(1, bookings.size());
    }

    @Test
    void findAllByUser_should_return_user_bookings() throws Exception {

        var userBookings = repo.findAllByUser("kayde@example.com");
        assertEquals(1, userBookings.size());
    }

    @Test
    void findAllByUser_with_invalid_user_should_return_no_bookings() throws Exception {

        var userBookings = repo.findAllByUser("jesse@example.com");
        assertEquals(0, userBookings.size());
    }

    @DirtiesContext
    @Test
    void findAllByClinic_should_return_clinic_bookings() throws Exception {
        var clinicBookings = repo.findAllByClinic("clinic1");
        assertEquals(2, clinicBookings.size());
    }
    @DirtiesContext
    @Test
    void findAllByClinic_with_invalid_clinic_should_return_no_bookings() throws Exception {

        var clinicBookings = repo.findAllByClinic("clinic3");
        assertEquals(0, clinicBookings.size());
    }

    @Test
    void update_should_update_selected_booking() throws Exception {

        // Original booking time was 10:00
        Booking updatedBooking = new Booking(
                null,
                "sonia@example.com",
                "owner1pet1",
                "2024-09-17",
                // Updated appointment time
                "12:30",
                "clinic1",
                "x-ray",
                "$150");
        repo.update(
                updatedBooking.owner(),
                updatedBooking.petName(),
                updatedBooking.date(),
                updatedBooking.time(),
                updatedBooking.location(),
                updatedBooking.visitReason(),
                updatedBooking.price(),
                1L);

        // Convert user bookings to string
        var userBookings = repo.findAllByUser("sonia@example.com").toString();
        assertTrue(userBookings.contains("12:30"));
    }

    @Test
    void getBookedTimes_returns_booked_times_within_8_days() throws Exception {

        LocalDate date = LocalDate.parse("2024-09-17");
        var bookedTimes = repo.getBookedTimes(date, "clinic1");
        assertEquals(2, bookedTimes.size());
    }

    @Test
    void getBookedTimes_returns_no_times_if_no_bookings_for_clinic() throws Exception {

        LocalDate date = LocalDate.parse("2024-09-17");
        var bookedTimes = repo.getBookedTimes(date, "clinic2");
        assertEquals(0, bookedTimes.size());
    }

    @Test
    void getBookedTimes_returns_no_times_if_no_bookings_in_range() throws Exception {


        repo.insertBooking(
                "jesse@example.com",
                "Sven",
                "2024-11-11",
                "11:00",
                "clinic1",
                "Check-up",
                "$35");

        LocalDate date = LocalDate.parse("2024-09-02");
        var bookedTimes = repo.getBookedTimes(date, "clinic1");
        assertEquals(0, bookedTimes.size());
    }

}