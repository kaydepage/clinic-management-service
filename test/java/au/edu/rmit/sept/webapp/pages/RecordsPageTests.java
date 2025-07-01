package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.controllers.RecordsController;
import au.edu.rmit.sept.webapp.models.MedRecord;
import au.edu.rmit.sept.webapp.models.VacRecord;
import au.edu.rmit.sept.webapp.models.Record;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.ui.Model;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecordsPageTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RecordService recordService;

    @InjectMocks
    private RecordsController recordsController;

    @MockBean
    private MockHttpSession session;

    @Mock
    private Model model;

    private User actualUser;
    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        actualUser = new User(
                1L,
                "Jesse",
                "Chalker",
                "jesse@example.com",
                "password1",
                false,
                null);

        testUser = new User(
                1L,
                "John",
                "Doe",
                "test@example.com",
                "password123",
                false,
                null);
    }

    @Test
    public void get_user_records() throws Exception {

        // Confirm the user's records in the database are shown on the page
        Collection<VacRecord> vacRecords = List.of(new VacRecord(
                "5",
                actualUser.email(),
                "Sven",
                "2024-09-26",
                "clinic2",
                "Rabies"));
        Collection<MedRecord> medRecords = List.of(new MedRecord(
                "1",
                actualUser.email(),
                "Sven",
                "2024-09-21",
                "clinic2",
                "General Checkup",
                "Antibiotics",
                "None"), new MedRecord(
                        "3",
                actualUser.email(),
                "Sven",
                "2024-09-21",
                "clinic2",
                "General Checkup",
                "Famotidine",
                "None"));

        when(session.getAttribute("user")).thenReturn(actualUser);

        when(recordService.getVacRecordsUser(actualUser.email())).thenReturn(vacRecords);
        when(recordService.getMedRecordsUser(actualUser.email())).thenReturn(medRecords);

        mockMvc.perform(get("/records").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("vacRecords", vacRecords))
                .andExpect(model().attribute("medRecords", medRecords))
                .andExpect(view().name("records/records"));
    }

    @Test
    public void user_not_logged_in_redirects() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/records").session(session))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void create_new_vaccination_record() throws Exception {
        Record record = new Record(
                testUser.email(),
                "Max", "2024-09-05",
                "clinic1",
                "vaccination",
                "N/A",
                "N/A");

        // Login as the test user
        when(session.getAttribute("user")).thenReturn(testUser);

        mockMvc.perform(put("/records")
                        .param("owner", record.owner())
                        .param("petName", record.petName())
                        .param("date", record.date())
                        .param("location", record.location())
                        .param("description", record.description())
                        .param("prescription", record.prescription())
                        .param("treatment", record.treatment()))
                .andExpect(redirectedUrl("/records"));

        mockMvc.perform(get("/records").session(session))
                .andExpect(status().isOk())

                // There are five records already hard-coded in the database
                .andExpect(model().attribute("vacRecords", List.of(new VacRecord("6", record.owner(), record.petName(), record.date(), record.location(), record.prescription()))))
                .andExpect(model().attribute("medRecords", List.of()))
                .andExpect(view().name("records/records"));


    }
}
