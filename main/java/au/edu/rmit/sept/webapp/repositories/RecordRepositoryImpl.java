package au.edu.rmit.sept.webapp.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.MedRecord;
import au.edu.rmit.sept.webapp.models.VacRecord;

@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecordRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS records ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "owner VARCHAR(255) not null, "
                + "petName VARCHAR(255) not null, "
                + "date VARCHAR(255) not null, "
                + "location VARCHAR(255) not null, "
                + "description VARCHAR(255) not null, "
                + "prescription VARCHAR(255) not null, "
                + "treatment VARCHAR(255) not null)";
        try {
            jdbcTemplate.execute(createTable);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in createTable", e);
        }
    }

    @Override
    public void insertRecord(String owner, String petName, String date, String location, String description,
            String prescription, String treatment) {
        String sql = "INSERT INTO records (owner, petname, date, location, description, prescription, treatment) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            jdbcTemplate.update(sql, owner, petName, date, location, description, prescription, treatment);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in create", e);
        }
    }

    @Override
    public List<MedRecord> findAllMed() {
        try {
            String sql = "SELECT id, owner, petname, date, location, description, prescription, treatment FROM records WHERE NOT description = 'vaccination'";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new MedRecord(
                    rs.getString("id"),
                    rs.getString("owner"),
                    rs.getString("petname"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("prescription"),
                    rs.getString("treatment")));
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllMed", e);
        }
    }

    @Override
    public List<MedRecord> findAllMedUser(String user) {
        try {
            String sql = "SELECT id, owner, petname, date, location, description, prescription, treatment FROM records WHERE NOT description = 'vaccination' AND owner = ?";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new MedRecord(
                    rs.getString("id"),
                    rs.getString("owner"),
                    rs.getString("petname"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("prescription"),
                    rs.getString("treatment")),
                    user);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllMed", e);
        }
    }

    @Override
    public List<VacRecord> findAllVac() {
        try {
            String sql = "SELECT id, owner, petname, date, location, prescription FROM records WHERE description = 'vaccination'";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new VacRecord(
                    rs.getString("id"),
                    rs.getString("owner"),
                    rs.getString("petname"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("prescription")));
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllVac", e);
        }
    }

    @Override
    public List<VacRecord> findAllVacUser(String user) {
        try {
            String sql = "SELECT id, owner, petname, date, location, prescription FROM records WHERE description = 'vaccination' AND owner = ?";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new VacRecord(
                    rs.getString("id"),
                    rs.getString("owner"),
                    rs.getString("petname"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("prescription")),
                    user);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllVac", e);
        }
    }
}