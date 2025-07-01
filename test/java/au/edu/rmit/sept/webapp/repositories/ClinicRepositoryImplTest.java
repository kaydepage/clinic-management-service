package au.edu.rmit.sept.webapp.repositories;


import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.ClinicServiceImpl;
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
public class ClinicRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @Mock
    private ClinicRepository repo;

    @MockBean
    private ClinicService service;

    @BeforeEach
    public void setUp() {
        repo = new ClinicRepositoryImpl(source);

        // Repo will contain nine entries for each clinic's three services
        service = new ClinicServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS clinics");
        service.initaliseDB();
    }

    @AfterEach
    public void tearDown() {
        // Remove any new information made in tests
        source.execute("DROP TABLE IF EXISTS clinics");
    }

    @Test
    void test_findPricesByClinic() throws Exception {
        var prices = repo.findPricesByClinic("clinic1");
        assertEquals(3, prices.size());
    }

    @Test
    void test_insertClinicService_with_new_clinic() throws Exception {
        // Make a test clinic service using a new clinic name
        repo.InsertClinicService("clinic4", "vaccination", "$49");

        var prices = repo.findPricesByClinic("clinic4");
        assertEquals(1, prices.size());
    }

    @Test
    void test_insertClinicService_with_existing_clinic() throws Exception {
        // Make a test clinic service using a new clinic name
        repo.InsertClinicService("clinic1", "new service", "$49");

        var prices = repo.findPricesByClinic("clinic1");
        assertEquals(4, prices.size());
    }
}