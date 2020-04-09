package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AuthorRepository;
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
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.Student;
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
public class CooperatorServiceEmployerContactTests extends BaseServiceTest {

    @Autowired EmployerContactService employerContactService;
    @Autowired CompanyService companyService;
    @Autowired CoopDetailsService coopDetailsService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CoopService coopService;
    @Autowired StudentService studentService;

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired AuthorRepository authorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        coopDetailsRepository.deleteAll();
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            coopDetailsService.deleteCoopDetails(cd);
        }
        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        authorRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void testCreateEmployerContact() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            fail();
        }

        c = companyService.getCompany(c.getId());
        assertEquals(firstName, c.getEmployees().get(0).getFirstName());
        assertEquals(1, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testEmployerContactUniqueEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
            // email must be unique so expect a SQLException
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (Exception e) {
            assertEquals(1, employerContactService.getAllEmployerContacts().size());
        }
    }

    @Test
    public void testCreateEmployerContactInvalidEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "abcdefg";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Employer Contact email must be a valid email!", e.getMessage());
        }
    }

    @Test
    public void testCreateEmployerContactInvalidPhoneNumber() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "asdfdgf";
        Company c = createTestCompany(companyService);

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Employer Contact phone number must be a valid number!",
                    e.getMessage());
        }
    }

    @Test
    public void testCreateEmployerContactNull() {
        String firstName = null;
        String lastName = null;
        String email = null;
        String phoneNumber = null;
        Company c = null;

        String error = "";

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                ERROR_PREFIX
                        + "Employer Contact first name cannot be empty!"
                        + " Employer Contact last name cannot be empty!"
                        + " Employer Contact email cannot be empty!"
                        + " Employer Contact phone number cannot be empty!"
                        + " Employer Contact company cannot be empty!",
                error);
        assertEquals(0, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testCreateEmployerContactEmpty() {
        String firstName = "";
        String lastName = "";
        String email = "";
        String phoneNumber = "";
        Company c = null;

        String error = "";

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                ERROR_PREFIX
                        + "Employer Contact first name cannot be empty!"
                        + " Employer Contact last name cannot be empty!"
                        + " Employer Contact email cannot be empty!"
                        + " Employer Contact phone number cannot be empty!"
                        + " Employer Contact company cannot be empty!",
                error);
        assertEquals(0, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testCreateEmployerContactSpaces() {
        String firstName = "  ";
        String lastName = " ";
        String email = "    ";
        String phoneNumber = "       ";
        Company c = null;

        String error = "";

        try {
            employerContactService.createEmployerContact(
                    firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                ERROR_PREFIX
                        + "Employer Contact first name cannot be empty!"
                        + " Employer Contact last name cannot be empty!"
                        + " Employer Contact email cannot be empty!"
                        + " Employer Contact phone number cannot be empty!"
                        + " Employer Contact company cannot be empty!",
                error);
        assertEquals(0, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testUpdateEmployerContactWithCoopDetails() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);
        EmployerContact ec = null;

        // course->course offering->coops->coop details
        CoopDetails cd = null;
        Course course = createTestCourse(courseService);
        CourseOffering co = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, co, s);
        Set<CoopDetails> coopDetails = new HashSet<CoopDetails>();

        try {
            ec =
                    employerContactService.createEmployerContact(
                            firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());

            cd = createTestCoopDetails(coopDetailsService, ec, coop);
            coopDetails.add(cd);

            Set<Report> reports = new HashSet<Report>();

            ec =
                    employerContactService.updateEmployerContact(
                            ec, firstName, lastName, email, phoneNumber, c, reports, coopDetails);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, ec.getCoopDetails().size());
        assertEquals(lastName, ec.getLastName());
        assertEquals(1, employerContactService.getAllEmployerContacts().size());
        c = companyService.getCompany(c.getId());
        assertEquals(firstName, c.getEmployees().get(0).getFirstName());
        cd = coopDetailsService.getCoopDetails(cd.getId());
        assertEquals(firstName, cd.getEmployerContact().getFirstName());
    }

    @Test
    public void testUpdateEmployerContact() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        EmployerContact ec = null;
        try {
            ec =
                    employerContactService.createEmployerContact(
                            firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }

        firstName = "Emma";
        lastName = "Eagles";
        email = "jeagles@gmail.com";
        phoneNumber = "9876543210";
        Set<Report> reports = new HashSet<Report>();
        Set<CoopDetails> coopDetails = new HashSet<CoopDetails>();

        try {
            ec =
                    employerContactService.updateEmployerContact(
                            ec, firstName, lastName, email, phoneNumber, c, reports, coopDetails);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(firstName, ec.getFirstName());
        assertEquals(1, employerContactService.getAllEmployerContacts().size());
        c = companyService.getCompany(c.getId());
        assertEquals(firstName, c.getEmployees().get(0).getFirstName());
    }

    @Test
    public void testUpdateEmployerContactInvalid() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        EmployerContact ec = null;
        try {
            ec =
                    employerContactService.createEmployerContact(
                            firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            employerContactService.updateEmployerContact(
                    ec, "", "   ", "", "    ", null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Employer Contact first name cannot be empty!"
                        + " Employer Contact last name cannot be empty!"
                        + " Employer Contact email cannot be empty!"
                        + " Employer Contact phone number cannot be empty!",
                error);

        // original EmployerContact should still exist
        assertEquals(1, employerContactService.getAllEmployerContacts().size());
        try {
            employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException _e) {
            fail();
        }
    }

    @Test
    public void testDeleteEmployerContact() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany(companyService);

        EmployerContact ec = null;
        try {
            ec =
                    employerContactService.createEmployerContact(
                            firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
            employerContactService.deleteEmployerContact(ec);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testDeleteEmployerContactInvalid() {
        String error = "";
        try {
            employerContactService.deleteEmployerContact(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Employer Contact to delete cannot be null!", error);
    }
}
