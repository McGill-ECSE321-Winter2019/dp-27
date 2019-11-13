package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCoopDetailsTests {

    // TODO: add Service and Repository class imports here

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // TODO: clear every repository here
    }

    @Test
    public void testCreateCoopDetails() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopDetailsNull() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopDetailsEmpty() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopDetailsSpaces() {
        assertTrue(true);
    }

    @Test
    public void testCreateCoopDetailsInvalid() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCoopDetails() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCoopDetailsInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteCoopDetails() {
        assertTrue(true);
    }
}
