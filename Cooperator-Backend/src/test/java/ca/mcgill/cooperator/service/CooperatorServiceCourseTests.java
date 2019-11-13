package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCourseTests {

    // TODO: add Service and Repository class imports here

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        // TODO: clear every repository here
    }

    @Test
    public void testCreateCourse() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseNull() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseEmpty() {
        assertTrue(true);
    }

    @Test
    public void testCreateCourseSpaces() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCourse() {
        assertTrue(true);
    }

    @Test
    public void testUpdateCourseInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteCourse() {
        assertTrue(true);
    }
}
