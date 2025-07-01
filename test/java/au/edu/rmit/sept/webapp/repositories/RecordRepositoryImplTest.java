package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.services.RecordService;
import au.edu.rmit.sept.webapp.services.RecordServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecordRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @Mock
    private RecordRepository repo;

    @MockBean
    private RecordService service;

    @BeforeEach
    public void setUp() {
        repo = new RecordRepositoryImpl(source);
        service = new RecordServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS records");
        service.initaliseDB();
    }

    @AfterEach
    public void tearDown() {
        source.execute("DROP TABLE IF EXISTS records");
    }


    @Test
    void findAllMed_should_return_med_records() throws Exception {
        var medRecords = repo.findAllMed();
        assertEquals(2, medRecords.size());
    }


    @Test
    void findAllVac_should_return_vac_records() throws Exception {
        var vacRecords = repo.findAllVac();
        assertEquals(3, vacRecords.size());
    }


    @Test
    void insertRecord_should_add_record_to_repo() throws Exception {
        repo.insertRecord(
                "Jesse",
                "Sven",
                "2024-10-12",
                "clinic2",
                "X-ray",
                "None",
                "None");

        var medRecords = repo.findAllMed();
        assertEquals(3, medRecords.size());
    }


    @Test
    void insertRecord_should_add_vaccination_to_repo() throws Exception {
        repo.insertRecord(
                "Jesse",
                "Sven",
                "2024-10-12",
                "clinic2",
                "vaccination",
                "None",
                "None");

        repo.insertRecord(
                "Jesse",
                "Sven",
                "2024-10-16",
                "clinic2",
                "check up",
                "Antibiotics",
                "None");

        var vacRecords = repo.findAllVac();
        assertEquals(4, vacRecords.size());
    }
}
