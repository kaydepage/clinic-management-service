package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import java.util.Collection;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrder(Order order) {
        String sql = "INSERT INTO orders (prescriberLocation, ownerFirstName, streetAddress, city, state, postcode, prescriptionName, quantity, dosageInfo, totalPrice) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.prescriberLocation(), order.ownerFirstName(), 
                            order.streetAddress(), order.city(), order.state(), 
                            order.postcode(), order.prescriptionName(), order.dosageInfo(), order.quantity(), order.totalPrice());
    }

    @Override
    public void createTable() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS orders (" +
                "prescriberLocation VARCHAR(255) NOT NULL, " +
                "ownerFirstName VARCHAR(255) NOT NULL, " +
                "streetAddress VARCHAR(255) NOT NULL, " +
                "city VARCHAR(255) NOT NULL, " +
                "state VARCHAR(255) NOT NULL, " +
                "postcode VARCHAR(255) NOT NULL, " +
                "prescriptionName VARCHAR(255) NOT NULL, " +
                "quantity VARCHAR(255) NOT NULL, " +
                "dosageInfo VARCHAR(255) NOT NULL, " +
                "totalPrice DOUBLE NOT NULL)";

        try {
            jdbcTemplate.execute(createTableSql);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error while creating orders table", e);
        }
    }

    @Override
    public Collection<Order> findAllByClinic(String clinic) {
        String sql = "SELECT * FROM orders WHERE prescriberLocation = ?;";

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                return new Order(
                        rs.getString("prescriberLocation"),
                        rs.getString("ownerFirstName"),
                        rs.getString("streetAddress"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("postcode"),
                        rs.getString("prescriptionName"),
                        rs.getString("quantity"),
                        rs.getString("dosageInfo"),
                        rs.getDouble("totalPrice"));
            }, clinic);
        } catch (DataAccessException e) {
            throw new UncategorizedScriptException("Error in findAllByClinic", e);
        }
    }

}
