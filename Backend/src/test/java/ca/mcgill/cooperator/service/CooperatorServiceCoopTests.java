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
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCoopTests extends BaseServiceTest {

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
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    public void testCreateCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student student = createTestStudent(studentService);

        try {
            coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        courseOffering = courseOfferingService.getCourseOfferingById(courseOffering.getId());
        assertEquals(status, courseOffering.getCoops().get(0).getStatus());
        student = studentService.getStudentById(student.getId());
        assertEquals(status, ((Coop) student.getCoops().toArray()[0]).getStatus());
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
                ERROR_PREFIX
                        + "Co-op Status cannot be null. "
                        + "Course Offering cannot be null. "
                        + "Student cannot be null.",
                error);
    }

    @Test
    public void testUpdateCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student student = createTestStudent(studentService);
        Coop c = new Coop();
        try {
            c = coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopService.getAllCoops().size());

        status = CoopStatus.COMPLETED;
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        CoopDetails cd = createTestCoopDetails(coopDetailsService, ec, c);
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

        courseOffering = courseOfferingService.getCourseOfferingById(courseOffering.getId());
        assertEquals(status, courseOffering.getCoops().get(0).getStatus());
        student = studentService.getStudentById(student.getId());
        assertEquals(status, ((Coop) student.getCoops().toArray()[0]).getStatus());
        cd = coopDetailsService.getCoopDetails(cd.getId());
        assertEquals(status, cd.getCoop().getStatus());
        assertEquals(status, c.getStatus());
        assertEquals(1, coopService.getAllCoops().size());
    }

    @Test
    public void testUpdateCoopInvalid() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student student = createTestStudent(studentService);

        try {
            coopService.createCoop(status, courseOffering, student);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, coopService.getAllCoops().size());
        String error = "";

        try {
            coopService.updateCoop(null, null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(1, coopService.getAllCoops().size());
        assertEquals(ERROR_PREFIX + "Co-op to update cannot be null!", error);
    }

    @Test
    public void testDeleteCoop() {
        CoopStatus status = CoopStatus.IN_PROGRESS;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student student = createTestStudent(studentService);
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
}
