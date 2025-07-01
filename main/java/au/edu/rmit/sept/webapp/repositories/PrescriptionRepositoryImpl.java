package au.edu.rmit.sept.webapp.repositories;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Prescription;

@Repository
public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public PrescriptionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS prescription ("
                + "owner VARCHAR(255) NOT NULL, "
                + "petName VARCHAR(255) NOT NULL, "
                + "name VARCHAR(255) NOT NULL, "
                + "dateOfIssue VARCHAR(255) NOT NULL, "
                + "prescriberLocation VARCHAR(255) NOT NULL, "
                + "description VARCHAR(255) NOT NULL, "
                + "quantity VARCHAR(255) NOT NULL, "
                + "dosageInfo VARCHAR(255) NOT NULL, "
                + "totalPrice DOUBLE NOT NULL)";
        try {
            jdbcTemplate.execute(createTable);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in createTable", e);
        }
    }


    @Override
    public void insertPrescription(String owner, String petName, String name, String dateOfIssue, 
                                   String prescriberLocation, String description, 
                                   String quantity, String dosageInfo, Double totalPrice) {
        String sql = "INSERT INTO prescription (owner, petName, name, dateOfIssue, prescriberLocation, description, quantity, dosageInfo, totalPrice) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            jdbcTemplate.update(sql, owner, petName, name, dateOfIssue, prescriberLocation, description, quantity, dosageInfo, totalPrice);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error inserting prescription", e);
        }
    }

@Override
public Prescription findByNameAndOwner(String name, String owner) {
    String sql = "SELECT * FROM prescription WHERE name = ? AND owner = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{name, owner}, (rs, rowNum) ->
            new Prescription(
                rs.getString("owner"),
                rs.getString("petName"),
                rs.getString("name"),
                rs.getString("dateOfIssue"),
                rs.getString("prescriberLocation"),
                rs.getString("description"),
                rs.getString("quantity"),
                rs.getString("dosageInfo"),
                rs.getDouble("totalPrice")
            )
    );
}

@Override
public Prescription findRefillByNameAndOwner(String name, String owner) {
    String sql = "SELECT * FROM prescription WHERE name = ? AND owner = ? LIMIT 1";
    
        return jdbcTemplate.queryForObject(sql, new Object[]{name, owner}, (rs, rowNum) ->
                new Prescription(
                    rs.getString("owner"),
                    rs.getString("petName"),
                    rs.getString("name"),
                    rs.getString("dateOfIssue"),
                    rs.getString("prescriberLocation"),
                    rs.getString("description"),
                    rs.getString("quantity"),
                    rs.getString("dosageInfo"),
                    rs.getDouble("totalPrice")
                )
        );
    
}

@Override
public List<Prescription> findPrescriptionsByOwner(String owner) {
    String sql = "SELECT * FROM prescription WHERE owner = ?";
    return jdbcTemplate.query(sql, (rs, rowNum) -> new Prescription(
            rs.getString("owner"),
            rs.getString("petName"),
            rs.getString("name"),
            rs.getString("dateOfIssue"),
            rs.getString("prescriberLocation"),
            rs.getString("description"),
            rs.getString("quantity"),
            rs.getString("dosageInfo"),
            rs.getDouble("totalPrice")
    ), owner);
}

}
