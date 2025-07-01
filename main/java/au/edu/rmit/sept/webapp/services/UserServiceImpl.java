package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.OrderRepository;
import au.edu.rmit.sept.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        userRepository.createTable();
        if (userRepository.getAllUsers().isEmpty()){
            userRepository.insertUser("Jesse", "Chalker", "jesse@example.com", "password1", false, null);

            userRepository.insertUser("Tim", "Betschel", "tim@example.com", "password2", false, null);

            userRepository.insertUser("Natalie", "Doan", "natalie@example.com", "password3", false, null);

            userRepository.insertUser("Sonia", "Nair", "sonia@example.com", "password4", false, null);

            userRepository.insertUser("Kayde", "Page", "kayde@example.com", "password5", false, null);

            userRepository.insertUser("Fadzly", "Tarmizi", "fadzly@example.com", "password6", false, null);

            userRepository.insertUser("Clinic", "1", "clinic1@example.com", "admin1", true, "clinic1");

            userRepository.insertUser("Clinic", "2", "clinic2@example.com", "admin2", true, "clinic2");

            userRepository.insertUser("Clinic", "3", "clinic3@example.com", "admin3", true, "clinic3");

        }
    }


    @Override
    public void createUser(String firstName, String lastName, String email, String password, boolean isAdmin, String location) {
        userRepository.insertUser(firstName, lastName, email, password, isAdmin, location);
    }

    @Override
    public User validateLogin(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password.equals(user.password())) {  // maybe we hash and compare passwords later
                return user;
            } else {
                throw new IllegalArgumentException("Invalid password");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
    @Override
    public boolean isUserAdmin(String email) {
    Boolean isAdmin = userRepository.findAdminStatusByEmail(email);
    return isAdmin != null && isAdmin; // Return true if user is admin, false if not or not found
    }
}