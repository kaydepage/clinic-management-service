package au.edu.rmit.sept.webapp.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import au.edu.rmit.sept.webapp.models.Booking;
import au.edu.rmit.sept.webapp.models.Clinic;

public interface ClinicRepository {
        public void createTable();

        public Collection<Clinic> findPricesByClinic(String clinic);

        public void InsertClinicService(String clinic, String service, String price);
}
