package au.edu.rmit.sept.webapp.services;

import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import au.edu.rmit.sept.webapp.models.Booking;

public interface BookingService {
    public Collection<Booking> getBookingsUser(String user);

    public Collection<Booking> getBookings();

    public Collection<Booking> getBookingsClinic(String clinic);

    public void create(String owner, String petName, String date, String time, String location, String visitReason,
            String price);

    public void deleteBookingById(Long id);

    public void initaliseDB();

    public List<LocalTime> getAvailableTimes(LocalDate date, String clinic);

    public void updateBooking(String owner, String petName, String date, String time, String location,
            String visitReason, String price, Long id);
}