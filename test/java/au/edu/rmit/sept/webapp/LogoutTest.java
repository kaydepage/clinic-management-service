package au.edu.rmit.sept.webapp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import au.edu.rmit.sept.webapp.controllers.LogoutController;
import au.edu.rmit.sept.webapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LogoutTest {

    @InjectMocks
    private LogoutController logoutController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MockHttpSession session;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L, "John", "Doe", "john@example.com", "password123", false, null);
    }

    @Test
    public void user_is_redirected_to_correct_page() throws Exception {
        when(session.getAttribute("user")).thenReturn(testUser);
        mockMvc.perform(get("/logout").session(session))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void user_not_logged_in_is_redirected_correctly() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        mockMvc.perform(get("/logout").session(session))
                .andExpect(redirectedUrl("/"));
    }
}
