package au.edu.rmit.sept.webapp.services;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.repositories.BookingRepository;
import jakarta.annotation.PostConstruct;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingRepository repository;

    @Autowired
    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initaliseDB() {
        repository.createTable();
        if (repository.findAll().isEmpty()) {
            repository.insertBooking("sonia@example.com",
                    "owner1pet1",
                    "2024-09-17",
                    "10:00",
                    "clinic1",
                    "x-ray",
                    "$150");
            repository.insertBooking("kayde@example.com",
                    "owner2pet1",
                    "2024-09-24",
                    "13:30",
                    "clinic1",
                    "vaccination",
                    "$25");
        }
    }

    @Override
    public Collection<Booking> getBookingsUser(String user) {
        return repository.findAllByUser(user);
    }

    @Override
    public Collection<Booking> getBookingsClinic(String clinic) {
        return repository.findAllByClinic(clinic);
    }

    @Override
    public Collection<Booking> getBookings() {
        return repository.findAll();
    }

    @Override
    public void create(String owner, String petName, String date, String time, String location, String visitReason,
            String price) {
        repository.insertBooking(owner, petName, date, time, location, visitReason, price);
    }

    @Override
    public void deleteBookingById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<LocalTime> getAvailableTimes(LocalDate date, String clinic) {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0); // start at 9 AM
        LocalTime endTime = LocalTime.of(13, 30); // end at 2 PM

        while (!startTime.isAfter(endTime)) {
            timeSlots.add(startTime);
            startTime = startTime.plusMinutes(30);
        }
        // System.out.println(timeSlots);

        List<LocalTime> booked = repository.getBookedTimes(date, clinic);
        timeSlots.removeAll(booked);
        // System.out.println(timeSlots);
        return timeSlots;

    }

    @Override
    public void updateBooking(String owner, String petName, String date, String time, String location,
            String visitReason, String price, Long id) {
        repository.update(owner, petName, date, time, location, visitReason, price, id);
    }
}