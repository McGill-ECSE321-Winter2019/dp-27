package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCourseOfferingTests {
	
	@Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;

    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	courseOfferingRepository.deleteAll();
    	courseRepository.deleteAll();
    }

    @Test
    public void testCreateCourseOffering() {
    	String name = "ECSE321";
    	int year = 2020;
    	Season season = Season.WINTER;
        Course c = courseService.createCourse(name);
        
        try {
        	CourseOffering co = courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }
        c = courseRepository.findByName(name);
        CourseOffering co = courseOfferingRepository.findByCourse(c).get(0);
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
       // assertEquals(co.getCourse(), c);
    }

    @Test
    public void testCreateCourseOfferingNull() {
    	try {
        	CourseOffering co = courseOfferingService.createCourseOffering(0, null, null);

        } catch (IllegalArgumentException e) {
        	assertEquals("Year is invalid! Season cannot be null! Course cannot be null!", e.getMessage());
        }
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 0);
    }

    @Test
    public void testUpdateCourseOffering() {
    	String name = "ECSE321";
    	int year = 2020;
    	Season season = Season.WINTER;
        Course c = courseService.createCourse(name);
        
        try {
        	courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }
        
        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        //assertEquals(co.getCourse(), c);
                
        String name2 = "ECSE223";
    	int year2 = 2021;
    	Season season2 = Season.FALL;
        Course c2 = courseService.createCourse(name);
        
        try {
        	courseOfferingService.updateCourseOffering(co, year2, season2, c2);

        } catch (IllegalArgumentException e) {
            fail();
        }
        
        co = courseOfferingService.getAllCourseOfferings().get(0);
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season2);
        assertEquals(co.getYear(), year2);
        //assertEquals(co.getCourse(), c2);
    }

    @Test
    public void testUpdateCourseOfferingInvalid() {
    	String name = "ECSE321";
    	int year = 2020;
    	Season season = Season.WINTER;
        Course c = courseService.createCourse(name);
        
        try {
        	courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }
        
        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
       // assertEquals(co.getCourse(), c);
                
        String name2 = null;
    	int year2 = -1;
    	Season season2 = null;
        Course c2 = null;
        
        try {
        	courseOfferingService.updateCourseOffering(co, year2, season2, c2);

        } catch (IllegalArgumentException e) {
        	assertEquals("Year is invalid! Season cannot be null! Course cannot be null!", e.getMessage());
        }
        
        co = courseOfferingService.getAllCourseOfferings().get(0);
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        //assertEquals(co.getCourse(), c);
    }

    @Test
    public void testDeleteCourseOffering() {
    	String name = "ECSE321";
    	int year = 2020;
    	Season season = Season.WINTER;
        Course c = courseService.createCourse(name);
        
        try {
        	courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }
        
        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        
        try {
        	courseOfferingService.deleteCourseOffering(co);
        } catch (IllegalArgumentException e) {
            fail();
        }
        
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 0);
    }
}
