package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.repositories.PetRepository;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;  
import au.edu.rmit.sept.webapp.models.Pet;  

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        petRepository.createTable();
        if (petRepository.getAllPets().isEmpty()) {
            petRepository.insertPet("Sven", "Rottweiler", 1, "Jesse", "Chalker");
        }
    }

    @Override
    public void createPet(String name, String breed, int age, String ownerFirstName, String ownerLastName) {
        petRepository.insertPet(name, breed, age, ownerFirstName, ownerLastName);
    }

    @Override
    public List<Pet> getPetsByOwner(String ownerFirstName) {
        return petRepository.findPetByOwner(ownerFirstName);
    }
    
}