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
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Student;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCoopDetailsTests extends BaseServiceTest {

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
        int payPerHour = 2000;
        int hoursPerWeek = 40;
        Date startDate = Date.valueOf("2020-05-11");
        Date endDate = Date.valueOf("2020-07-31");
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        try {
            coopDetailsService.createCoopDetails(
                    payPerHour, hoursPerWeek, startDate, endDate, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        ec = employerContactService.getEmployerContact(ec.getId());
        assertEquals(2000, ((CoopDetails) ec.getCoopDetails().toArray()[0]).getPayPerHour());
        coop = coopService.getCoopById(coop.getId());
        assertEquals(2000, coop.getCoopDetails().getPayPerHour());
        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testCreateCoopDetailsNull() {
        int payPerHour = 20;
        int hoursPerWeek = 40;
        String error = "";
        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Start date cannot be null! "
                        + "End date cannot be null! "
                        + "Employer Contact cannot be null! "
                        + "Co-op cannot be null!",
                error);
        assertEquals(0, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testCreateCoopDetailsInvalid() {
        int payPerHour = -1;
        int hoursPerWeek = -20;
        String error = "";
        try {
            coopDetailsService.createCoopDetails(payPerHour, hoursPerWeek, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Pay Per Hour is invalid! "
                        + "Hours Per Week is invalid! "
                        + "Start date cannot be null! "
                        + "End date cannot be null! "
                        + "Employer Contact cannot be null! "
                        + "Co-op cannot be null!",
                error);
        assertEquals(0, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testUpdateCoopDetails() {
        CoopDetails cd = null;
        int payPerHour = 2000;
        int hoursPerWeek = 40;
        Date startDate = Date.valueOf("2020-05-11");
        Date endDate = Date.valueOf("2020-07-31");
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        try {
            cd =
                    coopDetailsService.createCoopDetails(
                            payPerHour, hoursPerWeek, startDate, endDate, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());

        startDate = Date.valueOf("2020-06-12");
        endDate = Date.valueOf("2020-08-30");

        try {
            cd = coopDetailsService.updateCoopDetails(cd, 2200, 30, startDate, endDate, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        ec = employerContactService.getEmployerContact(ec.getId());
        assertEquals(2200, ((CoopDetails) ec.getCoopDetails().toArray()[0]).getPayPerHour());
        coop = coopService.getCoopById(coop.getId());
        assertEquals(2200, coop.getCoopDetails().getPayPerHour());
        assertEquals(2200, cd.getPayPerHour());
        assertEquals(Date.valueOf("2020-06-12"), cd.getStartDate());
        assertEquals(Date.valueOf("2020-08-30"), cd.getEndDate());
        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testUpdateCoopDetailsInvalid() {
        CoopDetails cd = null;
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Date startDate = Date.valueOf("2020-05-11");
        Date endDate = Date.valueOf("2020-07-31");
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        try {
            coopDetailsService.createCoopDetails(
                    payPerHour, hoursPerWeek, startDate, endDate, ec, coop);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopDetailsService.getAllCoopDetails().size());

        String error = "";
        try {
            coopDetailsService.updateCoopDetails(cd, -1, -222, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Co-op Details to update cannot be null!", error);
        assertEquals(1, coopDetailsService.getAllCoopDetails().size());
    }

    @Test
    public void testDeleteCoopDetails() {
        CoopDetails cd = null;
        int payPerHour = 20;
        int hoursPerWeek = 40;
        Date startDate = Date.valueOf("2020-05-11");
        Date endDate = Date.valueOf("2020-07-31");
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        try {
            cd =
                    coopDetailsService.createCoopDetails(
                            payPerHour, hoursPerWeek, startDate, endDate, ec, coop);
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

        assertEquals(ERROR_PREFIX + "Co-op Details to delete cannot be null!", error);
    }
}
