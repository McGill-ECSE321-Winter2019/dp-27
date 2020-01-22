package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Coop;
//TODO
@SpringBootTest
public class CooperatorServiceCoopTests {

    @Autowired
    private CoopService coopService;

    @Autowired 
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseOfferingService courseOfferingService;

    @Autowired
    private CoopRepository coopRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        studentRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        coopRepository.deleteAll();
    }

    @Test
    public void testCreateCoop() {
        CoopStatus status = CoopStatus.COMPLETED;
        Course c = createTestCourse();
        CourseOffering course = createTestCourseOffering(c);
        Student student = createTestStudent();

    	try {
    		coopService.createCoop(status, course, student);
    	}catch(IllegalArgumentException e) {
    		fail();
    	}
        assertTrue(true);
    }

    @Test
    public void testCreateCoopNull() {
        String error = "";
        CoopStatus status = null;
        CourseOffering course = null;
        Student student = null;

        try {
    		coopService.createCoop(status, course, student);
    	}catch(IllegalArgumentException e) {
            error = e.getMessage();
    	}
        assertEquals("Co-op Status cannot be null. Course Offering cannot be null. Student cannot be null.", error.trim());
    }

    @Test
    public void testUpdateCoop() {
        Coop coop = new Coop();
        CoopStatus status = CoopStatus.COMPLETED;
        Course c = createTestCourse();
        CourseOffering course = createTestCourseOffering(c);
        Student student = createTestStudent();

    	try {
    		coop = coopService.createCoop(status, course, student);
    	}catch(IllegalArgumentException e) {
    		fail();
        }
        
        status = CoopStatus.IN_PROGRESS;
        student = createTestStudent2();

        try{
            coopService.updateCoop(coop, status, course, null);
        }catch(IllegalArgumentException e){
            fail();
        }
        assertEquals(null, coopRepository.findByStudent("Susan"));
        assertEquals(1, coopRepository.findByStudent("Albert").size());
    }

    @Test
    public void testDeleteCoop() {
        Coop coop = new Coop();
        CoopStatus status = CoopStatus.COMPLETED;
        Course c = createTestCourse();
        CourseOffering course = createTestCourseOffering(c);
        Student student = createTestStudent();

    	try {
    		coop = coopService.createCoop(status, course, student);
    	}catch(IllegalArgumentException e) {
    		fail();
        }

        try {
    		coopService.deleteCoop(coop);
    	}catch(IllegalArgumentException e) {
    		fail();
        }

        assertEquals(0, coopRepository.count());
    }

    private Course createTestCourse() {
        Course c = new Course();
        c = courseService.createCourse("FACC201");
        return c;
    }

    private CourseOffering createTestCourseOffering(Course c) {
        CourseOffering co = new CourseOffering();
        co = courseOfferingService.createCourseOffering(2020, Season.WINTER, c);
        return co;
    }
    private Student createTestStudent() {
        Student s = new Student();
        s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
    }

    private Student createTestStudent2() {
        Student s = new Student();
        s = studentService.createStudent("Albert", "Kragl", "ak47@gmail.com", "260222150");

        return s;
    }
}