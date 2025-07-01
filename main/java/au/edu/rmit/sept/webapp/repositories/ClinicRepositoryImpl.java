package au.edu.rmit.sept.webapp.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Collection;

import javax.management.RuntimeErrorException;

import org.springframework.jdbc.datasource.init.UncategorizedScriptException;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import au.edu.rmit.sept.webapp.models.Clinic;

@Repository
public class ClinicRepositoryImpl implements ClinicRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClinicRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS clinics ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "clinic VARCHAR(255) not null, "
                + "service VARCHAR(255) not null, "
                + "price VARCHAR(255) not null)";

        try {
            jdbcTemplate.execute(createTable);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in createTable", e);
        }
    }

    @Override
    public Collection<Clinic> findPricesByClinic(String clinic) {
        String sql = "SELECT * FROM clinics WHERE clinic = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                return new Clinic(
                        rs.getLong("id"),
                        rs.getString("clinic"),
                        rs.getString("service"),
                        rs.getString("price"));
            }, clinic);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findPricesByClinic", e);
        }
    }

    @Override
    public void InsertClinicService(String clinic, String service, String price) {
        String sql = "INSERT INTO clinics (clinic, service, price) VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql, clinic, service, price);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in create", e);
        }
    }

}
