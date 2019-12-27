package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceEmployerContactTests {

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


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	coopDetailsRepository.deleteAll();
    	List <CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
    	for(CoopDetails cd : coopDetails) {
    		coopDetailsService.deleteCoopDetails(cd);
    	}
    			
    	coopRepository.deleteAll();
    	courseOfferingRepository.deleteAll();
    	courseRepository.deleteAll();
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
        Company c = createTestCompany();
        
        try {
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(2, employerContactService.getAllEmployerContacts().size());
    }
    
    @Test
    public void testCreateEmployerContactInvalidEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "abcdefg";
        String phoneNumber = "0123456789";
        Company c = createTestCompany();

        try {
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            assertEquals("Employer Contact email must be a valid email!", e.getMessage());
        }
    }
    
    @Test
    public void testCreateEmployerContactInvalidPhoneNumber() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "asdfdgf";
        Company c = createTestCompany();

        try {
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            assertEquals("Employer Contact phone number must be a valid number!", e.getMessage());
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
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                "Employer Contact first name cannot be empty!"
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
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                "Employer Contact first name cannot be empty!"
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
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals(
                "Employer Contact first name cannot be empty!"
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
        Company c = createTestCompany();
        EmployerContact ec = null;
        
        //course->course offering->coops->coop details
        CoopDetails cd = null;
        Course course = createTestCourse();
        CourseOffering co = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(co, s);
        List<CoopDetails> coopDetails = new ArrayList<CoopDetails>();
        
        try {
            ec = employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
            
            cd = createTestCoopDetails(ec, coop);
            coopDetails.add(cd);
            
            List<EmployerReport> reports = new ArrayList<EmployerReport>();
            
            ec = employerContactService.updateEmployerContact(ec, firstName, lastName, email, phoneNumber, c, reports, coopDetails);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, ec.getCoopDetails().size());
        assertEquals(lastName, ec.getLastName());
        assertEquals(2, employerContactService.getAllEmployerContacts().size());
        
    }

    @Test
    public void testUpdateEmployerContact() {
    	String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany();
        
        EmployerContact ec = null;
        try {
            ec = employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }
        
        firstName = "Emma";
        lastName = "Eagles";
        email = "jeagles@gmail.com";
        phoneNumber = "9876543210";
        List<EmployerReport> reports = new ArrayList<EmployerReport>();
        List<CoopDetails> coopDetails = new ArrayList<CoopDetails>();
        
        try {
        	ec = employerContactService.updateEmployerContact(ec, firstName, lastName, email, phoneNumber, c, 
        												 reports, coopDetails);
        	ec = employerContactService.getEmployerContact(ec.getId());	
        } catch (IllegalArgumentException e) {
        	fail();
        }
        
        assertEquals(firstName, ec.getFirstName());
        assertEquals(2, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testUpdateEmployerContactInvalid() {
    	String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany();
        
        EmployerContact ec = null;
        try {
            ec = employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(ec.getId());
        } catch (IllegalArgumentException e) {
            fail();
        }
        
        String error = "";
        try {
        	employerContactService.updateEmployerContact(ec, "", "   ", "", "    ", null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Employer Contact first name cannot be empty!"
                + " Employer Contact last name cannot be empty!"
                + " Employer Contact email cannot be empty!"
                + " Employer Contact phone number cannot be empty!"
                + " Employer Contact company cannot be null!"
                + " Employer Contact employer reports cannot be null!"
                + " Employer Contact coop details cannot be null!",
                error);

        // original EmployerContact should still exist (and employer contact created for test company)
        assertEquals(2, employerContactService.getAllEmployerContacts().size());
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
        Company c = createTestCompany();
        
        EmployerContact ec = null;
        try {
        	ec = employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        	ec = employerContactService.getEmployerContact(ec.getId());
        	employerContactService.deleteEmployerContact(ec);
        } catch (IllegalArgumentException e) {
            fail();
        }
        
        assertEquals(1, employerContactService.getAllEmployerContacts().size());
    }

    @Test
    public void testDeleteEmployerContactInvalid() {
    	String error = "";
        try {
        	employerContactService.deleteEmployerContact(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Employer Contact to delete cannot be null!", error);
    }
    
    private Company createTestCompany() {
        Company c = new Company();
        c = companyService.createCompany("Facebook", new ArrayList<EmployerContact>());
        
        return c;
    }
    
    private CoopDetails createTestCoopDetails(EmployerContact ec, Coop c) {
    	CoopDetails cd = new CoopDetails();
    	cd = coopDetailsService.createCoopDetails(20, 40, ec, c);
    	return cd;
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
    
    private Coop createTestCoop(CourseOffering co, Student s) {
    	Coop coop = new Coop();
    	coop = coopService.createCoop(CoopStatus.FUTURE, co, s);
    	return coop;
    }

}
