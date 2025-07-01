package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.UserService;
import au.edu.rmit.sept.webapp.services.UserServiceImpl;
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
public class UserRepositoryImplTest {

    @Autowired
    JdbcTemplate source;

    @Mock
    private UserRepository repo;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        repo = new UserRepositoryImpl(source);
        userService = new UserServiceImpl(repo);

        source.execute("DROP TABLE IF EXISTS admin_users");
        source.execute("DROP TABLE IF EXISTS users");
        userService.initializeDatabase();
    }

    @AfterEach
    public void tearDown() {
        source.execute("DROP TABLE IF EXISTS admin_users");
        source.execute("DROP TABLE IF EXISTS users");
    }


    @Test
    void getAllUsers_should_return_all_users() throws Exception {
        var users = repo.getAllUsers();
        assertEquals(9, users.size());
    }


    @Test
    void insertUser_should_add_user_to_repository() throws Exception {
        repo.insertUser(
                "test1_first_name",
                "test1_last_name",
                "test1@example.com",
                "password",
                false,
                null
        );
        repo.insertUser(
                "test2_first_name",
                "test2_last_name",
                "test2@example.com",
                "password",
                true,
                null
        );

        var users = repo.getAllUsers();
        assertEquals(11, users.size());
    }


    @Test
    void findByEmail_should_return_true_if_user_exists() throws Exception {
        var userExists = repo.findByEmail("jesse@example.com").isPresent();
        assertTrue(userExists);
    }


    @Test
    void findByEmail_should_return_false_if_no_user_exists() throws Exception {
        var userExists = repo.findByEmail("test@example.com").isPresent();
        assertFalse(userExists);
    }


    @Test
    void test_findAdminStatusByEmail_with_non_admin() throws Exception {
        var isAdmin = repo.findAdminStatusByEmail("jesse@example.com");
        assertFalse(isAdmin);
    }


    @Test
    void test_findAdminStatusByEmail_with_admin() throws Exception {

        var isAdmin = repo.findAdminStatusByEmail("clinic1@example.com");
        assertTrue(isAdmin);
    }
}
