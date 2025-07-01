package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import au.edu.rmit.sept.webapp.services.PrescriptionServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
public class PrescriptionRepositoryImplTest {
    @Autowired
    JdbcTemplate source;

    @Mock
    private PrescriptionRepository repo;

    @MockBean
    private PrescriptionService service;

    @BeforeEach
    public void setUp() {
        repo = new PrescriptionRepositoryImpl(source);
        service = new PrescriptionServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS prescription");
        service.initaliseDB();
    }

    @AfterEach
    public void tearDown() {
        source.execute("DROP TABLE IF EXISTS prescription");
    }

    @Test
    void findPrescriptionsByOwner_returns_prescriptions() throws Exception {
        var prescriptions = repo.findPrescriptionsByOwner("jesse@example.com");
        assertEquals(2, prescriptions.size());
    }

    @Test
    void insertPrescription_adds_prescription_to_repo() throws Exception {
        repo.insertPrescription(
                "jesse@example.com",
                "Sven",
                "test_name",
                "2024-09-22",
                "clinic2",
                "Give 1/2 pill for every meal",
                "25",
                "250mg",
                25.99);

        var prescriptions = repo.findPrescriptionsByOwner("jesse@example.com");
        assertEquals(3, prescriptions.size());
    }

    @Test
    void findPrescriptionsByOwner_returns_no_prescriptions_for_nonexistent_owner() throws Exception {
        var prescriptions = repo.findPrescriptionsByOwner("nonexistent");
        assertEquals(0, prescriptions.size());
    }

    @Test
    void test_findByNameAndOwner() throws Exception {
        var result = repo.findByNameAndOwner("Antibiotics", "jesse@example.com");

        // This is the expected prescription to be returned
        Prescription prescription = new Prescription(
                "jesse@example.com",
                "Sven",
                "Antibiotics",
                "2024-09-21",
                "clinic2",
                "Give 1 pill every morning.",
                "30",
                "500mg",
                20.99);
        assertEquals(prescription, result);
    }
}
