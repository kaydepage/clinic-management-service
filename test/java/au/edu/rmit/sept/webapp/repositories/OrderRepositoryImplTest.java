package au.edu.rmit.sept.webapp.repositories;


import au.edu.rmit.sept.webapp.services.OrderService;
import au.edu.rmit.sept.webapp.services.OrderServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import au.edu.rmit.sept.webapp.models.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @Mock
    private OrderRepository repo;

    @MockBean
    private OrderService service;

    @BeforeEach
    public void setUp() {
        repo = new OrderRepositoryImpl(source);

        service = new OrderServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS orders");
        service.initaliseDB();
    }

    @AfterEach
    public void tearDown() {
        // Remove any new orders made in tests
        source.execute("DROP TABLE IF EXISTS orders");
    }


    @Test
    void test_findAllByClinic() throws Exception {

        // No orders exist in database, so there should be no bookings returned
        var orders = repo.findAllByClinic("clinic1");
        assertEquals(0, orders.size());
    }

    @Test
    void test_save_order() throws Exception {
        // Create a new order to add to the database
        Order order = new Order(
                "clinic1",
                "John",
                "123 Example Street",
                "Melbourne",
                "VIC",
                "1234",
                "Test",
                "100",
                "100mg",
                20.00
                );
        repo.saveOrder(order);
        var orders = repo.findAllByClinic("clinic1");
        assertEquals(1, orders.size());
    }
}