package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ResourcesPageTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MockHttpSession session;

    private User testUser;
    private User actualUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "Doe", "john@example.com", "password123", true, null);
        actualUser = new User(1L, "Kayde", "Page", "kayde@example.com", "password123", false, null);
    }

    @Test
    public void user_not_logged_in_gets_redirected_from_resources_page() throws Exception {
        // Try to access the page as a user not logged in
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/eduresources").session(session))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void admin_user_gets_redirected_from_resources_page() throws Exception {
        // Log in as an admin user
        when(session.getAttribute("user")).thenReturn(testUser);

        mockMvc.perform(get("/eduresources").session(session))
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    public void ed_resources_page_loads() throws Exception {
        // Log in as a pet owner user
        when(session.getAttribute("user")).thenReturn(actualUser);

        // Check the page displays one of the titles in the page
        mockMvc.perform(get("/eduresources").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("What is Pet Care?")));
    }
}
