package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.controllers.HomeController;
import au.edu.rmit.sept.webapp.models.User;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HomePageTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private HomeController homeController;

    @MockBean
    private MockHttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_user_logged_in() throws Exception {

        // Create a dummy user to enter the page
        User user = new User(
                1L,
                "Test",
                "User",
                "testuser@example.com",
                "password123",
                false,
                null);
        when(session.getAttribute("user")).thenReturn(user);

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Welcome, Test!"))
                .andExpect(view().name("welcome/index"));
    }

    @Test
    public void test_user_not_logged_in() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        // Ensure the user is directed to the login/sign-up page
        mockMvc.perform(get("/home").session(session))
                .andExpect(redirectedUrl("/"));
    }
}
