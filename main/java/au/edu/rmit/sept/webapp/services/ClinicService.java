package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import au.edu.rmit.sept.webapp.models.Clinic;

public interface ClinicService {
        public void initaliseDB();

        public Collection<Clinic> getPricesClinic(String clinic);

        public void create(String clinic, String service, String price);
}