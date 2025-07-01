package au.edu.rmit.sept.webapp.services;

import java.util.Collection;

import au.edu.rmit.sept.webapp.models.VacRecord;
import au.edu.rmit.sept.webapp.models.MedRecord;

public interface RecordService {
    public Collection<MedRecord> getMedRecords();

    public Collection<VacRecord> getVacRecords();

    public Collection<MedRecord> getMedRecordsUser(String user);

    public Collection<VacRecord> getVacRecordsUser(String user);

    public void initaliseDB();

    public void createRecord(String owner, String petName, String date, String location, String description,
            String prescription, String treatment);
}