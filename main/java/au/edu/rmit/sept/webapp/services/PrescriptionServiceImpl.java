package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.ArrayList;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public List<Prescription> getPrescriptionInfo(String owner) {
    List<Prescription> prescriptions = prescriptionRepository.findPrescriptionsByOwner(owner);
    return (prescriptions != null) ? prescriptions : new ArrayList<>();  // Return an empty list if no prescriptions are found
    }


    @PostConstruct
    public void initaliseDB() {
        prescriptionRepository.createTable();
        if (prescriptionRepository.findPrescriptionsByOwner("jesse@example.com").isEmpty()) {
            prescriptionRepository.insertPrescription("tim@example.com",
                    "Okashi",
                    "Antibiotics",
                    "2024-09-13",
                    "clinic1",
                    "Give 1 pill every morning.",
                    "30",
                    "500mg",
                    30.99);
            prescriptionRepository.insertPrescription("jesse@example.com",
                    "Sven",
                    "Antibiotics",
                    "2024-09-21",
                    "clinic2",
                    "Give 1 pill every morning.",
                    "30",
                    "500mg",
                    20.99);
            prescriptionRepository.insertPrescription("jesse@example.com",
                    "Sven",
                    "Famotidine",
                    "2024-09-22",
                    "clinic2",
                    "Give 1/2 pill every 24 hours.",
                    "20",
                    "250mg",
                    25.50);
        }
    }

    @Override
    public void insertPrescription(String owner, String petName, String name, String dateOfIssue, 
                                   String prescriberLocation, String description, String quantity, String dosageInfo, Double totalPrice) {
        // Call the repository to insert the prescription into the database
        prescriptionRepository.insertPrescription(owner, petName, name, dateOfIssue, 
                                                  prescriberLocation, description, quantity, dosageInfo, totalPrice);
    }

    @Override
public Prescription getPrescriptionByNameAndOwner(String name, String owner) {
    return prescriptionRepository.findByNameAndOwner(name, owner);
}

   @Override
public Prescription getRefillPrescriptionByNameAndOwner(String name, String owner){
    return prescriptionRepository.findRefillByNameAndOwner(name, owner);
}

}