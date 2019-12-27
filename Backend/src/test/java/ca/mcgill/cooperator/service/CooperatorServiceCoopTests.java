package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCoopTests {

    // TODO: add Service and Repository class imports here

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // TODO: clear every repository here
    }

    @Test
    public void testCreateCoop() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopNull() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopEmpty() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopSpaces() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCoop() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCoopInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteCoop() {
        assertTrue(true);
    }
}
