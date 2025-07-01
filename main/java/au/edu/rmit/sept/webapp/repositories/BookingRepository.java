package au.edu.rmit.sept.webapp.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import au.edu.rmit.sept.webapp.models.Booking;

public interface BookingRepository {
        public void createTable();

        public Collection<Booking> findAllByUser(String user);

        public Collection<Booking> findAll();

        public Collection<Booking> findAllByClinic(String clinic);

        public void insertBooking(String owner, String petName, String date, String time, String location,
                        String visitReason, String price);

        public void deleteById(Long id);

        public void update(String owner, String petName, String date, String time, String location,
                        String visitReason, String price, Long id);

        public List<LocalTime> getBookedTimes(LocalDate date, String clinic);
}
