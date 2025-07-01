package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void createTable();
    void insertUser(String firstName, String lastName, String email, String password, boolean isAdmin, String location);
    List<String> getAllUsers();
    Optional<User> findByEmail(String email);
    Boolean findAdminStatusByEmail(String email);
    void insertAdminUser(Long userId, String location);      
}