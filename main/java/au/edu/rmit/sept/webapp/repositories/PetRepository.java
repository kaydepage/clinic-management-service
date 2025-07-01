package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
    void createTable();
    void insertPet(String name, String breed, int age, String ownerFirstName, String ownerLastName);
    List<Pet> getAllPets();
    List<Pet> findPetByOwner(String ownerFirstName);
    List<Pet> findPetByName(String name);
}