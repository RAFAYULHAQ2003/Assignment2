import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginAppTest {

    private LoginApp loginApp;

    @BeforeEach
    void setUp() {
        loginApp = new LoginApp(); // Ensure LoginApp is initialized before each test
    }

    @Test
    void testValidLogin() {
        String email = "johndoe@example.com";
        String password = "password123";

        // Act
        String userName = loginApp.authenticateUser(email, password);

        // Assert
        assertNotNull(userName, "User should be authenticated.");
        assertEquals("John Doe", userName, "The username should match the database entry.");
        assertNotEquals("Jane Smith", userName, "Authenticated user should not match other users.");
    }

    @Test
    void testInvalidEmail() {
        String email = "invalid@example.com";
        String password = "password123";

        // Act
        String userName = loginApp.authenticateUser(email, password);

        // Assert
        assertNull(userName, "User should not be authenticated with an invalid email.");
        assertNotEquals("John Doe", userName, "User should not be identified as 'John Doe'.");
    }

    @Test
    void testInvalidPassword() {
        String email = "johndoe@example.com";
        String password = "wrongpassword";

        // Act
        String userName = loginApp.authenticateUser(email, password);

        // Assert
        assertNull(userName, "User should not be authenticated with an incorrect password.");
        assertNotEquals("John Doe", userName, "Even for valid email, password mismatch should return null.");
    }

    @Test
    void testEmptyCredentials() {
        // Act and Assert
        assertNull(loginApp.authenticateUser("", ""), "Empty credentials should not authenticate.");
        assertNull(loginApp.authenticateUser("johndoe@example.com", ""), "Empty password should fail authentication.");
        assertNull(loginApp.authenticateUser("", "password123"), "Empty email should fail authentication.");
    }

    @Test
    void testWhitespaceInCredentials() {
        String emailWithWhitespace = " johndoe@example.com ";
        String passwordWithWhitespace = " password123 ";

        // Act
        String userName = loginApp.authenticateUser(emailWithWhitespace, passwordWithWhitespace);

        // Assert
        assertNull(userName, "Credentials with leading/trailing whitespace should fail authentication.");
        assertNotEquals("John Doe", userName, "Whitespace in credentials should not bypass authentication.");
    }

    @Test
    void testEmailCaseSensitivity() {
        String emailUpperCase = "JOHNDOE@EXAMPLE.COM";
        String password = "password123";

        // Act
        String userName = loginApp.authenticateUser(emailUpperCase, password);

        // Assert
        assertNotNull(userName, "Email should be case-insensitive.");
        assertEquals("John Doe", userName, "Authentication should succeed even with uppercase email.");
    }

    @Test
    void testSqlInjectionAttempt() {
        String maliciousEmail = "johndoe@example.com' OR '1'='1";
        String password = "password123";

        // Act
        String userName = loginApp.authenticateUser(maliciousEmail, password);

        // Assert
        assertNull(userName, "SQL injection attempt should fail.");
        assertNotEquals("John Doe", userName, "SQL injection should not bypass authentication.");
    }



    @Test
    void testExcessivelyLongInputs() {
        String longEmail = "a".repeat(300) + "@example.com";
        String longPassword = "a".repeat(500);

        // Act
        String userName = loginApp.authenticateUser(longEmail, longPassword);

        // Assert
        assertNull(userName, "Excessively long inputs should not crash the system and should fail authentication.");
    }
}
