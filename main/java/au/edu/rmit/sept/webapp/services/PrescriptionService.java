package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Prescription;
import java.util.List;

public interface PrescriptionService {
    List<Prescription> getPrescriptionInfo(String ownerFirstName);
    
    public void initaliseDB();
    
    public Prescription getPrescriptionByNameAndOwner(String name, String owner);

    public Prescription getRefillPrescriptionByNameAndOwner(String name, String owner);

    public void insertPrescription(String owner, String petName, String prescriptionName, String dateOfIssue, String prescriberLocation, String description, String quantity, String dosageInfo, Double totalPrice);
}

    