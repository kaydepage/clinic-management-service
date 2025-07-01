package au.edu.rmit.sept.webapp.models;

public record Pet(Long id, String name, 
              String breed, 
              int age, 
              String ownerFirstName, 
              String ownerLastName) {
    
}