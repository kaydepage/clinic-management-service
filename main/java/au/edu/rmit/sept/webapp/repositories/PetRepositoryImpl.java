package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Pet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PetRepositoryImpl implements PetRepository {

    private final JdbcTemplate jdbcTemplate;

    public PetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS pets ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255), "
                + "breed VARCHAR(255), "
                + "age INT, "
                + "ownerFirstName VARCHAR(255), "
                + "ownerLastName VARCHAR(255))";

        jdbcTemplate.execute(createTableSQL);
    }

    @Override
    public void insertPet(String name, String breed, int age, String ownerFirstName, String ownerLastName) {
        String insertDataSQL = "INSERT INTO pets (name, breed, age, ownerFirstName, ownerLastName) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertDataSQL, name, breed, age, ownerFirstName, ownerLastName);
    }

    @Override
    public List<Pet> getAllPets() {
        String sql = "SELECT * FROM pets";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Pet(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("breed"),
                    rs.getInt("age"),
                    rs.getString("ownerFirstName"),
                    rs.getString("ownerLastName"))
            );
        } catch (DataAccessException e) {
            throw new DataAccessException("Error in getAllPets", e) {};
        }
    }

    @Override
    public List<Pet> findPetByOwner(String ownerFirstName) {
        String sql = "SELECT * FROM pets WHERE ownerFirstName = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Pet(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("breed"),
                    rs.getInt("age"),
                    rs.getString("ownerFirstName"),
                    rs.getString("ownerLastName")),
                    ownerFirstName
            );
        } catch (DataAccessException e) {
            throw new DataAccessException("Error in findPetByOwner", e) {};
        }
    }

    @Override
    public List<Pet> findPetByName(String name) {
        String sql = "SELECT * FROM pets WHERE name = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Pet(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("breed"),
                    rs.getInt("age"),
                    rs.getString("ownerFirstName"),
                    rs.getString("ownerLastName")),
                    name
            );
        } catch (DataAccessException e) {
            throw new DataAccessException("Error in findPetByName", e) {};
        }
    }
}