package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import au.edu.rmit.sept.webapp.models.Prescription;

public interface PrescriptionRepository {
    public void createTable();

    public List<Prescription> findPrescriptionsByOwner(String owner);

    public void insertPrescription(String owner, String petName, String name, String dateOfIssue, 
                               String prescriberLocation, String description, 
                               String quantity, String dosageInfo, Double totalPrice);
                               
    public Prescription findByNameAndOwner(String name, String owner);

    public Prescription findRefillByNameAndOwner(String name, String owner);

}
