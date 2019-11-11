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
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;

@SpringBootTest
public class CooperatorServiceEmployerContactTests {

	@Autowired EmployerContactService employerContactService;
	@Autowired CompanyService companyService;

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	employerContactRepository.deleteAll();
    	companyRepository.deleteAll();
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
    public void testCreateEmployerContactWithCoopDetails() {
    	String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        String phoneNumber = "0123456789";
        Company c = createTestCompany();
        EmployerContact ec = null;
        CoopDetails cd = createTestCoopDetails();
        List<CoopDetails> coopDetails = new ArrayList<CoopDetails>();
        
        try {
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(email);
            
            cd = createTestCoopDetails();
            coopDetails.add(cd);
            
            List<EmployerReport> reports = new ArrayList<EmployerReport>();
            
            employerContactService.updateEmployerContact(ec, firstName, lastName, email, phoneNumber, c, reports, coopDetails);
            ec = employerContactService.getEmployerContact(email);
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
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(email);
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
        	employerContactService.updateEmployerContact(ec, firstName, lastName, email, phoneNumber, c, 
        												 reports, coopDetails);
        	ec = employerContactService.getEmployerContact(email);	
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
            employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
            ec = employerContactService.getEmployerContact(email);
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
        	employerContactService.getEmployerContact(email);
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
        	employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, c);
        	ec = employerContactService.getEmployerContact(email);
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
        EmployerContact ec = new EmployerContact();
        ec.setEmail("albert@kragl.com");
        ec.setFirstName("Albert");
        ec.setLastName("Kragl");
        ec.setPhoneNumber("123456789");
        
        String name = "Facebook";
        List<EmployerContact> employers = new ArrayList<EmployerContact>();
        employers.add(ec);
        c = companyService.createCompany(name, employers);
        return c;
    }
    
    private static CoopDetails createTestCoopDetails() {
    	CoopDetails cd = new CoopDetails();
    	cd.setHoursPerWeek(40);
    	cd.setPayPerHour(20);
    	return cd;
    }

}
