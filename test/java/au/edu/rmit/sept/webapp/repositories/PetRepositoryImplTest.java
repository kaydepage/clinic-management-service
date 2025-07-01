package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.PetServiceImpl;
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
public class PetRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @Mock
    private PetRepository repo;

    @MockBean
    private PetService service;

    @BeforeEach
    public void setUp() {
        repo = new PetRepositoryImpl(source);
        service = new PetServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS pets");
        service.initializeDatabase();
    }

    @AfterEach
    public void tearDown() {
        // Remove any new pets made in tests
        source.execute("DROP TABLE IF EXISTS pets");
    }

    @Test
    void getAllPets_returns_all_pets() throws Exception {
        var pets = repo.getAllPets();
        assertEquals(1, pets.size());
    }

    @DirtiesContext
    @Test
    void insertPet_adds_pet_to_repository() throws Exception {
        repo.insertPet(
                "Benny",
                "Beagle",
                3,
                "Jesse",
                "Chalker");
        var pets = repo.getAllPets();
        assertEquals(2, pets.size());
    }

    @Test
    void findPetByOwner_returns_owners_pets() throws Exception {
        var pets = repo.findPetByOwner("Jesse");
        assertEquals(1, pets.size());
    }

    @Test
    void findPetByOwner_returns_no_pets_if_owner_has_no_pets() throws Exception {
        var pets = repo.findPetByOwner("Tim");
        assertEquals(0, pets.size());
    }

    @Test
    void findPetByName_returns_pet() throws Exception {
        var pet = repo.findPetByName("Sven");
        assertEquals(1, pet.size());
    }

    @Test
    void findPetByName_returns_no_pet_if_name_not_found() throws Exception {

        var pet = repo.findPetByName("Rex");
        assertEquals(0, pet.size());
    }
}
