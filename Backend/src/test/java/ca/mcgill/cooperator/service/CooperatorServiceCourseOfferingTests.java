package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCourseOfferingTests {

    // TODO: add Service and Repository class imports here

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // TODO: clear every repository here
    }

    @Test
    public void testCreateCourseOffering() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseOfferingNull() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseOfferingEmpty() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseOfferingSpaces() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCourseOffering() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCourseOfferingInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteCourseOffering() {
        assertTrue(true);
    }
}
