package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCoopDetailsTests {

    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired StudentRepository studentRepository;

    @Autowired CoopDetailsService coopDetailsService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CompanyService companyService;
    @Autowired EmployerContactService employerContactService;
    @Autowired StudentService studentService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }
        coopDetailsRepository.deleteAll();
        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void testCreateCoopDetails() {
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);

        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testCreateCoopDetailsNull() {
        int payPerHour = 20;
        int hoursPerWeek = 40;
        String error = "";
        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Employer Contact cannot be null! " + "Co-op cannot be null!", error);
        assertEquals(0, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testCreateCoopDetailsInvalid() {
        int payPerHour = -1;
        int hoursPerWeek = -20;
        String error = "";
        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Pay Per Hour is invalid! "
                        + "Hours Per Week is invalid! "
                        + "Employer Contact cannot be null! "
                        + "Co-op cannot be null!",
                error);
        assertEquals(0, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testUpdateCoopDetails() {
        CoopDetails cd = null;
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);

        try {
            cd = coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());

        try {
            cd = coopDetailsService.updateCoopDetails(cd, 22, 30, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(22, cd.getPayPerHour());
        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testUpdateCoopDetailsInvalid() {
        CoopDetails cd = null;
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);

        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());

        String error = "";
        try {
            coopDetailsService.updateCoopDetails(cd, -1, -222, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Co-op Details to update cannot be null!", error);
        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testDeleteCoopDetails() {
        CoopDetails cd = null;
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);

        try {
            cd = coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());

        try {
            coopDetailsService.deleteCoopDetails(cd);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testDeleteCoopDetailsInvalid() {
        String error = "";
        try {
            coopDetailsService.deleteCoopDetails(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Co-op Details to delete cannot be null!", error);
    }

    private Course createTestCourse() {
        Course c = null;
        c = courseService.createCourse("FACC200");
        return c;
    }

    private CourseOffering createTestCourseOffering(Course c) {
        CourseOffering co = null;
        co = courseOfferingService.createCourseOffering(2020, Season.WINTER, c);
        return co;
    }

    private Coop createTestCoop(CourseOffering co, Student s) {
        Coop coop = new Coop();
        coop = coopService.createCoop(CoopStatus.FUTURE, co, s);
        return coop;
    }

    private Company createTestCompany() {
        Company c = new Company();
        c =
                companyService.createCompany(
                        "Facebook",
                        "Menlo Park",
                        "California",
                        "USA",
                        new ArrayList<EmployerContact>());

        return c;
    }

    private EmployerContact createTestEmployerContact(Company c) {
        EmployerContact ec = new EmployerContact();
        ec =
                employerContactService.createEmployerContact(
                        "Emma", "Eags", "eags@gmail.com", "2143546578", c);
        return ec;
    }

    private Student createTestStudent() {
        Student s = new Student();
        s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
    }
}
