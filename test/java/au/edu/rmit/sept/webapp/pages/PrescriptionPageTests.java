package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.controllers.PrescriptionController;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrescriptionPageTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    @MockBean
    private MockHttpSession session;

    @Mock
    private Model model;

    private User testUser;
    private User actualUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "Doe", "john.doe@example.com", "password123", false, null);
        actualUser = new User(1L, "Jesse", "Chalker", "jesse@example.com", "password1", false, null);
    }

    @Test
    public void user_with_prescriptions_returns_correctly() throws Exception {
        // This list contains existing user's hard-coded prescriptions in the database
        List<Prescription> prescriptions = List.of(
                new Prescription(
                        actualUser.email(),
                        "Sven",
                        "Antibiotics",
                        "2024-09-21",
                        "clinic2",
                        "Give 1 pill every morning.",
                        "30",
                        "500mg",
                        20.99),
                new Prescription(
                        actualUser.email(),
                        "Sven",
                        "Famotidine",
                        "2024-09-22",
                        "clinic2",
                        "Give 1/2 pill every 24 hours.",
                        "20",
                        "250mg",
                        25.50)
        );

        when(session.getAttribute("user")).thenReturn(actualUser);
        when(prescriptionService.getPrescriptionInfo(testUser.email())).thenReturn(prescriptions);

        mockMvc.perform(get("/prescription").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("prescriptions", prescriptions))
                .andExpect(view().name("prescription/prescription"));
    }

    @Test
    public void user_with_no_prescriptions_return_correct_message() throws Exception {
        when(session.getAttribute("user")).thenReturn(testUser);

        mockMvc.perform(get("/prescription").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "No prescriptions available."))
                .andExpect(view().name("prescription/prescription"));
    }

    @Test
    public void user_not_logged_in_redirects_correctly() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/prescription").session(session))
                .andExpect(redirectedUrl("/"));
    }
}
