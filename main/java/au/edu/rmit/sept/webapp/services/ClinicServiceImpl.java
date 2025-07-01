package au.edu.rmit.sept.webapp.services;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.repositories.ClinicRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ClinicServiceImpl implements ClinicService {

    private ClinicRepository repository;

    @Autowired
    public ClinicServiceImpl(ClinicRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initaliseDB() {
        repository.createTable();
        if (repository.findPricesByClinic("clinic1").isEmpty()) {
            repository.InsertClinicService("clinic1",
                    "vaccination",
                    "$25");
            repository.InsertClinicService("clinic1",
                    "general consultation",
                    "$60");
            repository.InsertClinicService("clinic1",
                    "x-ray",
                    "$150");

            repository.InsertClinicService("clinic2",
                    "vaccination",
                    "$40");
            repository.InsertClinicService("clinic2",
                    "general consultation",
                    "$80");
            repository.InsertClinicService("clinic2",
                    "x-ray",
                    "$200");

            repository.InsertClinicService("clinic3",
                    "vaccination",
                    "$60");
            repository.InsertClinicService("clinic3",
                    "general consultation",
                    "$105");
            repository.InsertClinicService("clinic3",
                    "x-ray",
                    "$310");
        }
    }

    @Override
    public Collection<Clinic> getPricesClinic(String clinic) {
        return repository.findPricesByClinic(clinic);
    }

    @Override
    public void create(String clinic, String service, String price) {
        repository.InsertClinicService(clinic, service, price);
    }

}