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

import au.edu.rmit.sept.webapp.models.Booking;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "owner VARCHAR(255) not null, "
                + "petName VARCHAR(255) not null, "
                + "date VARCHAR(255) not null, "
                + "time VARCHAR(255) not null, "
                + "location VARCHAR(255) not null, "
                + "visitReason VARCHAR(255) not null, "
                + "price VARCHAR(255) not null)";

        try {
            jdbcTemplate.execute(createTable);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in createTable", e);
        }
    }

    @Override
    public Collection<Booking> findAllByUser(String user) {
        String sql = "SELECT * FROM bookings WHERE owner = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                return new Booking(
                        rs.getLong("id"),
                        rs.getString("owner"),
                        rs.getString("petName"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("location"),
                        rs.getString("visitReason"),
                        rs.getString("price"));
            }, user);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllByUser", e);
        }
    }

    @Override
    public Collection<Booking> findAll() {
        String sql = "SELECT * FROM bookings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Booking(
                rs.getLong("id"),
                rs.getString("owner"),
                rs.getString("petName"),
                rs.getString("date"),
                rs.getString("time"),
                rs.getString("location"),
                rs.getString("visitReason"),
                rs.getString("price")));
    }

    @Override
    public Collection<Booking> findAllByClinic(String clinic) {
        String sql = "SELECT * FROM bookings WHERE location = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                return new Booking(
                        rs.getLong("id"),
                        rs.getString("owner"),
                        rs.getString("petName"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("location"),
                        rs.getString("visitReason"),
                        rs.getString("price"));
            }, clinic);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllByUser", e);
        }
    }

    @Override
    public void insertBooking(String owner, String petName, String date, String time, String location,
            String visitReason, String price) {
        String sql = "INSERT INTO bookings (owner, petname, date, time, location, visitReason, price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, owner, petName, date, time, location, visitReason, price);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in create", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try {
            int row = jdbcTemplate.update(sql, id);

            if (row == 0) {
                throw new RuntimeException("Failed to delete Booking by ID: " + id);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in deleteById", e);
        }
    }

    @Override
    public void update(String owner, String petName, String date, String time, String location,
            String visitReason, String price, Long id) {
        String sql = "UPDATE bookings SET owner = ?, petname = ?, date = ?, time = ?, location = ?, visitReason = ?, price = ? WHERE id = ?";

        try {
            int row = jdbcTemplate.update(sql,
                    owner,
                    petName,
                    date,
                    time,
                    location,
                    visitReason,
                    price,
                    id);

            if (row == 0) {
                throw new RuntimeException("Failed to update Booking with ID: " + id);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in update", e);
        }
    }

    @Override
    public List<LocalTime> getBookedTimes(LocalDate date, String clinic) {
        LocalDate endDate = date.plusDays(8);
        String sql = "SELECT time FROM bookings WHERE date BETWEEN ? AND ? AND location = ?";
        Object[] params = { date, endDate, clinic };
        try {
            return jdbcTemplate.query(
                    sql,
                    (rs, rowNum) -> {
                        String timeStr = rs.getString("time");
                        return LocalTime.parse(timeStr);
                    },
                    params);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error in getBookedTimes", e);
        }
    }

}
