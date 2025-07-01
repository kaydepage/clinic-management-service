package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.User;

public interface UserService {

    public void initializeDatabase();
    void createUser(String firstName, String lastName, String email, String password, boolean isAdmin, String location);
    User validateLogin(String email, String password);
    boolean isUserAdmin(String email);
}