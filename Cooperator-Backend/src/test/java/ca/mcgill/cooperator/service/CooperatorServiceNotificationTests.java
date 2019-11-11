package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceNotificationTests {

    // TODO: add Service and Repository class imports here

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // TODO: clear every repository here
    }

    @Test
    public void testCreateNotification() {
        assertTrue(true);
    }

    @Test
    public void testCreateNotificationNull() {
        assertTrue(true);
    }

    @Test
    public void testCreateNotificationEmpty() {
        assertTrue(true);
    }

    @Test
    public void testCreateNotificationSpaces() {
        assertTrue(true);
    }

    @Test
    public void testCreateNotificationOneStudent() {
        assertTrue(true);
    }

    @Test
    public void testCreateNotificationManyStudents() {
        assertTrue(true);
    }

    @Test
    public void testUpdateNotification() {
        assertTrue(true);
    }

    @Test
    public void testUpdateNotificationInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteNotification() {
        assertTrue(true);
    }
}
