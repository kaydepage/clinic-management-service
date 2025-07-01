package au.edu.rmit.sept.webapp.pages;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import au.edu.rmit.sept.webapp.controllers.ProfileController;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.PetService;
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

import java.util.Collection;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProfilePageTests {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MockHttpSession session;


    private User testUser;
    private User actualUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1L,"John", "Doe", "john@example.com", "password", false, null);
        actualUser = new User(1L, "Jesse", "Chalker", "jesse@example.com", "password1", false, null);
    }

    @Test
    public void returns_details_and_pets_of_logged_in_user() throws Exception {
        // Existing user has only one pet hard-coded in the database
        Collection<Pet> pets = List.of(new Pet(1L, "Sven", "Rottweiler", 1, "Jesse", "Chalker"));

        // Login as existing user
        when(session.getAttribute("user")).thenReturn(actualUser);

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile"))
                .andExpect(model().attribute("name", " " + actualUser.firstName() + " " + actualUser.lastName()))
                .andExpect(model().attribute("email", " " + actualUser.email()))
                .andExpect(model().attribute("pets", pets));
    }

    @Test
    public void user_not_logged_in_redirects_correctly() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        mockMvc.perform(get("/profile").session(new MockHttpSession()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void user_redirects_to_register_pet_correctly() throws Exception {
        when(session.getAttribute("user")).thenReturn(testUser);

        mockMvc.perform(get("/registerPet").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/registerPet"));
    }

    @Test
    public void user_not_logged_in_redirects_from_register_pet_correctly() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        mockMvc.perform(get("/registerPet").session(new MockHttpSession()))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void user_registers_new_pet() throws Exception {
        // ID is 2, since one pet has already been hard-coded in the database
        Pet pet = new Pet(2L, "Buddy", "Golden Retriever", 3, "John", "Doe");

        when(session.getAttribute("user")).thenReturn(testUser);
        doNothing().when(petService).createPet(pet.name(), pet.breed(), pet.age(), pet.ownerFirstName(), pet.ownerLastName());


        mockMvc.perform(post("/registerPet")
                        .session(session)
                        .param("name", pet.name())
                        .param("breed", pet.breed())
                        .param("age", "3"))
                .andExpect(redirectedUrl("/profile"));

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile"))
                .andExpect(model().attribute("name", " " + testUser.firstName() + " " + testUser.lastName()))
                .andExpect(model().attribute("email", " " + testUser.email()))
                .andExpect(model().attribute("pets", List.of(pet)));

    }
}
