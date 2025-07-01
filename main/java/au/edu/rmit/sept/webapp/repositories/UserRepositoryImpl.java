package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        // Create the users table if not exists
    String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "firstName VARCHAR(255), " +
            "lastName VARCHAR(255), " +
            "email VARCHAR(255), " +
            "password VARCHAR(255), " +
            "isAdmin BOOLEAN DEFAULT FALSE, " + 
            "location VARCHAR(255));";    
        jdbcTemplate.execute(createTableSQL);

        // Create the admin_users table if not exists
        String createAdminTableSQL = "CREATE TABLE IF NOT EXISTS admin_users (" +
                "admin_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT, " +
                "location VARCHAR(255), " +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
        jdbcTemplate.execute(createAdminTableSQL);
    }

    @Override
    @Transactional
    public void insertUser(String firstName, String lastName, String email, String password, boolean isAdmin, String location) {
        // Insert user into the users table
        String insertDataSQL = "INSERT INTO users (firstName, lastName, email, password, isAdmin, location) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertDataSQL, firstName, lastName, email, password, isAdmin, location);

        // If user is an admin, also insert them into the admin_users table with their location
        if (isAdmin) {
            String sql = "SELECT id FROM users WHERE email = ?";
            Long userId = jdbcTemplate.queryForObject(sql, new Object[]{email}, Long.class);
            insertAdminUser(userId, location);
        }
    }

    public void insertAdminUser(Long userId, String location) {
        String insertAdminSQL = "INSERT INTO admin_users (user_id, location) VALUES (?, ?)";
        jdbcTemplate.update(insertAdminSQL, userId, location);
    }



    @Override
    public List<String> getAllUsers() {
        String selectDataSQL = "SELECT firstName FROM users";
        return jdbcTemplate.query(selectDataSQL, (rs, rowNum) -> rs.getString("firstName"));
    }


   @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
    try {
        User user = jdbcTemplate.queryForObject(sql, new Object[]{email}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("password"), rs.getBoolean("isAdmin"), rs.getString("location"));
            }
        });
        return Optional.of(user);
    } catch (Exception e) {
        return Optional.empty();  // Return an empty Optional if the user is not found
    }
}

    @Override
    public Boolean findAdminStatusByEmail(String email) {
        String sql = "SELECT isAdmin FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, Boolean.class);
        } catch (EmptyResultDataAccessException e) {
            return null; // User not found
        }
    }

}