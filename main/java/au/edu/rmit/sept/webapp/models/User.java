package au.edu.rmit.sept.webapp.models;

public record User(Long id, String firstName, String lastName, String email, String password, boolean isAdmin, String location) {
    
}