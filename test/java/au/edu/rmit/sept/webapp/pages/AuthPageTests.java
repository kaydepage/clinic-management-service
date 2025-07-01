package au.edu.rmit.sept.webapp.pages;

import au.edu.rmit.sept.webapp.controllers.AuthController;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.UserService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthPageTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @MockBean
    private MockHttpSession session;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_AuthPage() throws Exception {
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(authController.AuthPage()));
    }

    @Test
    public void signup_success_redirects_to_home_page() throws Exception {

        // Signup using new user details
        String firstName = "John";
        String lastName = "Doe";
        String email = "newuser@example.com";
        String password = "1234";
        boolean isAdmin = false;
        String location = null;

        // ID is 10, since there are currently nine users hard-coded in the database
        User user = new User(
                10L,
                "John",
                "Doe",
                email,
                password,
                isAdmin,
                location);

        when(userService.validateLogin(email, password)).thenReturn(user);

        mockMvc.perform(post("/signup").session(session)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password)
                        )
                .andExpect(redirectedUrl("/home"));

        //verify(session).setAttribute("user", user);
    }

    @Test
    public void signup_with_existing_user_returns_correct_exception() throws Exception {

        // Attempt to signup with existing user details
        String firstName = "Jesse";
        String lastName = "Chalker";
        String email = "jesse@example.com";
        String password = "password1";
        boolean isAdmin = false;
        String location = null;

        mockMvc.perform(post("/signup").session(session)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password)
                        .param("isAdmin", String.valueOf(isAdmin))
                        .param("location", location))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Signup failed: User not found"))
                .andExpect(view().name("user-auth/signup-login"));
    }

    @Test
    public void login_success_redirects_to_home_page() throws Exception {

        // Login using details of existing user in database
        String email = "jesse@example.com";
        String password = "password1";

        // The ID is 1, since the existing user in the database has an ID of 1
        User user = new User(1L, "Jesse", "Chalker", email, password, false, null);

        when(userService.validateLogin(email, password)).thenReturn(user);

        mockMvc.perform(post("/login").session(session)
                        .param("email", email)
                        .param("password", password)
                        )
                .andExpect(redirectedUrl("/home"));

        //verify(session).setAttribute("user", user);
    }

    @Test
    public void login_success_redirects_admin_user_to_admin_dashboard() throws Exception {

        // Login using details of existing user in database
        String email = "clinic1@example.com";
        String password = "admin1";

        // The ID is 7, since the existing user in the database has an ID of 7
        User user = new User(7L, "Clinic", "1", email, password, true, "clinic1");

        when(userService.validateLogin(email, password)).thenReturn(user);

        mockMvc.perform(post("/login").session(session)
                        .param("email", email)
                        .param("password", password)
                        )
                .andExpect(redirectedUrl("/admin/dashboard"));

        //verify(session).setAttribute("user", user);
    }

    @Test
    public void login_with_wrong_password_returns_correct_exception() throws Exception {

        // Attempt to login with existing user in database but with wrong password
        String email = "jesse@example.com";
        String password = "wrongpassword";

        mockMvc.perform(post("/login").session(session)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Invalid password"))
                .andExpect(view().name("user-auth/signup-login"));
    }

    @Test
    public void login_failure_returns_correct_exception() throws Exception {

        // Attempt to login using details that don't exist in Users database
        String email = "testuser@example.com";
        String password = "password123";

        mockMvc.perform(post("/login").session(session)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "User not found"))
                .andExpect(view().name("user-auth/signup-login"));
    }
}
