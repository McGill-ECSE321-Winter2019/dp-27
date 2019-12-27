package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCourseTests {

	@Autowired CourseService courseService;

    @Autowired CourseRepository courseRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	courseRepository.deleteAll();
    }

    @Test
    public void testCreateCourse() {
        String name = "ECSE321";
        try {
            courseService.createCourse(name);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, courseService.getAllCourses().size());
        assertEquals(name, courseService.getAllCourses().get(0).getName()); 
    }

    @Test
    public void testCreateCourseNull() {
    	String name = null;
    	String error = "";
        try {
            courseService.createCourse(name);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        assertEquals(
                "Course name cannot be empty!", error);
        assertEquals(0, courseService.getAllCourses().size());
    }

    @Test
    public void testCreateCourseEmpty() {
    	String name = "";
    	String error = "";
        try {
            courseService.createCourse(name);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        assertEquals(
                "Course name cannot be empty!", error);
        assertEquals(0, courseService.getAllCourses().size());
    }

    @Test
    public void testCreateCourseSpaces() {
    	String name = "   ";
    	String error = "";
        try {
            courseService.createCourse(name);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        assertEquals(
                "Course name cannot be empty!", error);
        assertEquals(0, courseService.getAllCourses().size());
    }

    @Test
    public void testUpdateCourseInvalid() {
    	String error = "";
    	String name = "ECSE321";
    	Course c = courseService.createCourse(name);
        try {
            courseService.updateCourse(c, "");
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        assertEquals(
                "Course name cannot be empty!", error);
        assertEquals(1, courseService.getAllCourses().size());
        assertEquals(name, courseService.getAllCourses().get(0).getName()); 
    }

    @Test
    public void testUpdateCourse() {
    	String error = "";
    	String name1 = "ECSE321";
    	String name2 = "ECSE458";
    	Course c = courseService.createCourse(name1);
    	
    	assertEquals(name1, courseService.getAllCourses().get(0).getName()); 
    	
        try {
            courseService.updateCourse(c, name2);
        } catch (IllegalArgumentException e) {
        	fail();
        }
        
        assertEquals(1, courseService.getAllCourses().size());
        assertEquals(name2, courseService.getAllCourses().get(0).getName()); 
    }

    @Test
    public void testDeleteCourse() {
    	String name = "ECSE321";
        try {
            courseService.createCourse(name);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(1, courseService.getAllCourses().size());
        assertEquals(name, courseService.getAllCourses().get(0).getName()); 
        try {
            courseService.deleteCourse(courseService.getAllCourses().get(0));
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(0, courseService.getAllCourses().size());
    }
    
    @Test
    public void testDeleteCourseNull() {
    	String error = "";
        try {
            courseService.deleteCourse(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(error, "Course to delete cannot be null!");
    }
}
