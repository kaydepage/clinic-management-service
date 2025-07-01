package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import au.edu.rmit.sept.webapp.models.VacRecord;
import au.edu.rmit.sept.webapp.models.MedRecord;

public interface RecordRepository {
    public void createTable();

    public void insertRecord(String owner, String petName, String date, String location, String description,
            String prescription, String treatment);

    public List<MedRecord> findAllMed();

    public List<VacRecord> findAllVac();

    public List<MedRecord> findAllMedUser(String user);

    public List<VacRecord> findAllVacUser(String user);
}