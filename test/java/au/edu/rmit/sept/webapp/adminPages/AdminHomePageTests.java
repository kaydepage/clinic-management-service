package au.edu.rmit.sept.webapp.adminPages;

import au.edu.rmit.sept.webapp.controllers.AdminController;
import au.edu.rmit.sept.webapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminHomePageTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private MockHttpSession session;

    private User adminUser;
    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "Doe", "john.doe@example.com", "password123", false, null);
        adminUser = new User(1L, "Clinic", "1", "clinic1@example.com", "admin1", true, "clinic1");
    }

    @Test
    void test_page_works_on_login() throws Exception {
        when(session.getAttribute("user")).thenReturn(adminUser);

        mockMvc.perform(get("/admin/dashboard").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void test_page_redirects_user_that_is_not_an_admin() throws Exception {
        when(session.getAttribute("user")).thenReturn(testUser);

        mockMvc.perform(get("/admin/dashboard").session(session))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void test_page_redirects_user_that_is_not_logged_in() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/admin/dashboard").session(session))
                .andExpect(redirectedUrl("/"));
    }

}
