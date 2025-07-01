package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Pet;
import java.util.List;

public interface PetService {

    public void initializeDatabase();

    void createPet(String name, String breed, int age, String ownerFirstName, String ownerLastName);

    List<Pet> getPetsByOwner(String ownerFirstName);
}