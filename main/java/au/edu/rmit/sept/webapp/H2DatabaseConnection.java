package au.edu.rmit.sept.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DatabaseConnection {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb";
    private static final String JDBC_USER = "sept-08-05";
    private static final String JDBC_PASSWORD = "password0805";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}