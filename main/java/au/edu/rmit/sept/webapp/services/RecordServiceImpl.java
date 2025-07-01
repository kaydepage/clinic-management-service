package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.MedRecord;
import au.edu.rmit.sept.webapp.models.VacRecord;
import au.edu.rmit.sept.webapp.repositories.RecordRepository;
import jakarta.annotation.PostConstruct;

@Service
public class RecordServiceImpl implements RecordService {

    private RecordRepository repository;

    @Autowired
    public RecordServiceImpl(RecordRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initaliseDB() {
        repository.createTable();
        if (repository.findAllMed().isEmpty()) {
            repository.insertRecord("jesse@example.com",
                    "Sven",
                    "2024-09-21",
                    "clinic2",
                    "General Checkup",
                    "Antibiotics",
                    "None");
            repository.insertRecord("kayde@example.com",
                    "Benji",
                    "2024-09-14",
                    "clinic1",
                    "vaccination",
                    "Rabies",
                    "None");
            repository.insertRecord("jesse@example.com",
                    "Sven",
                    "2024-09-21",
                    "clinic2",
                    "General Checkup",
                    "Famotidine",
                    "None");
            repository.insertRecord("natalie@example.com",
                    "Max",
                    "2024-09-25",
                    "clinic1",
                    "vaccination",
                    "Rabies",
                    "None");
            repository.insertRecord("jesse@example.com",
                    "Sven",
                    "2024-09-26",
                    "clinic2",
                    "vaccination",
                    "Rabies",
                    "None");
        }
    }

    @Override
    public Collection<MedRecord> getMedRecords() {
        return repository.findAllMed();
    }

    @Override
    public Collection<VacRecord> getVacRecords() {
        return repository.findAllVac();
    }

    @Override
    public Collection<MedRecord> getMedRecordsUser(String user) {
        return repository.findAllMedUser(user);
    }

    @Override
    public Collection<VacRecord> getVacRecordsUser(String user) {
        return repository.findAllVacUser(user);
    }

    @Override
    public void createRecord(String owner, String petName, String date, String location, String description,
            String prescription, String treatment) {
        repository.insertRecord(owner, petName, date, location, description, prescription, treatment);
    }
}