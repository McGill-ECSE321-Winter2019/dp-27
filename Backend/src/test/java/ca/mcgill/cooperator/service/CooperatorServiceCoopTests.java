package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CooperatorServiceCoopTests {

    // TODO: add Service and Repository class imports here
    @Autowired CoopService coopService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired CourseService courseService;
    @Autowired CoopDetailsService coopDetailsService;
    @Autowired EmployerContactService employerContactService;
    @Autowired CompanyService companyService;

    @Autowired CoopRepository coopRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }
        coopDetailsRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        coopRepository.deleteAll();
        coopDetailsRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    public void testCreateCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student student = createTestStudent();

        try {
            coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertEquals(1, coopService.getAllCoops().size());
    }

    @Test
    public void testCreateCoopNull() {
        String error = "";
        try {
            coopService.createCoop(null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(0, coopService.getAllCoops().size());
        assertEquals(
                "Co-op Status cannot be null. "
                        + "Course Offering cannot be null. "
                        + "Student cannot be null.",
                error);
    }

    @Test
    public void testUpdateCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student student = createTestStudent();
        Coop c = new Coop();
        try {
            c = coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopService.getAllCoops().size());

        status = CoopStatus.COMPLETED;
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        CoopDetails cd = createTestCoopDetails(ec, c);
        Set<EmployerReport> employerReports = new HashSet<EmployerReport>();
        Set<StudentReport> studentReports = new HashSet<StudentReport>();

        try {
            c =
                    coopService.updateCoop(
                            c,
                            status,
                            courseOffering,
                            student,
                            cd,
                            employerReports,
                            studentReports);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(status, c.getStatus());
        assertEquals(1, coopService.getAllCoops().size());
    }

    @Test
    public void testUpdateCoopInvalid() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student student = createTestStudent();
        Coop c = new Coop();
        try {
            c = coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopService.getAllCoops().size());
        String error = "";

        try {
            c = coopService.updateCoop(c, null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(1, coopService.getAllCoops().size());
        assertEquals(
                "Co-op Status cannot be null! "
                        + "Course Offering cannot be null! "
                        + "Student cannot be null! "
                        + "Co-op Details cannot be null! "
                        + "Employer Reports cannot be null! "
                        + "Student Reports cannot be null!",
                error);
    }

    @Test
    public void testDeleteCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student student = createTestStudent();
        Coop c = new Coop();
        try {
            c = coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopService.getAllCoops().size());

        try {
            coopService.deleteCoop(c);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, coopService.getAllCoops().size());
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

    private Student createTestStudent() {
        Student s = new Student();
        s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
    }

    private CoopDetails createTestCoopDetails(EmployerContact ec, Coop c) {
        CoopDetails cd = new CoopDetails();
        cd = coopDetailsService.createCoopDetails(20, 40, ec, c);
        return cd;
    }

    private EmployerContact createTestEmployerContact(Company c) {
        EmployerContact ec;
        ec =
                employerContactService.createEmployerContact(
                        "Albert", "Kragl", "albert@gmail.com", "12345678", c);
        return ec;
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
}
