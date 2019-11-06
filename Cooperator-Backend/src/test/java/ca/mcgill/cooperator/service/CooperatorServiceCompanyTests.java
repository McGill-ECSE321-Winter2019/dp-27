package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;

@SpringBootTest
public class CooperatorServiceCompanyTests {

	@Autowired
	CompanyService companyService;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployerContactRepository employerContactRepository;
	
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	companyRepository.deleteAll();
    	employerContactRepository.deleteAll();
    }

    @Test
    public void testCreateCompany() {
        String name = "Facebook";
        EmployerContact e = createTestEmployerContact();
        List<EmployerContact> employees = new ArrayList<EmployerContact>();
        employees.add(e);
        
        Company c = null;
        try {
        	c = companyService.createCompany(name, employees);
        	
        	companyService.getCompany("Facebook");
        	companyService.getCompany(c.getId());
        } catch (IllegalArgumentException _e) {
        	fail();
        }
        
        assertEquals(companyService.getAllCompanies().size(), 1);
    }

    @Test
    public void testCreateCompanyNoEmployees() {
        String name = "Facebook";
        List<EmployerContact> employees = new ArrayList<EmployerContact>();
        
        String error = "";
        try {
        	companyService.createCompany(name, employees);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Company must have at least one EmployerContact!", error);
        assertEquals(companyService.getAllCompanies().size(), 0);
    }

    @Test
    public void testCreateCompanyNullEmployees() {
        String name = "Facebook";
        
        String error = "";
        try {
        	companyService.createCompany(name, null);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Company must have at least one EmployerContact!", error);
        assertEquals(companyService.getAllCompanies().size(), 0);
    }

    @Test
    public void testCreateCompanyNull() {
        String error = "";
        try {
        	companyService.createCompany(null, null);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Company name cannot be empty! Company must have at least one EmployerContact!", error);
        assertEquals(companyService.getAllCompanies().size(), 0);
    }
    
    @Test
    public void testCreateCompanySpacesName() {
        String error = "";
        try {
        	companyService.createCompany("  ", null);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Company name cannot be empty! Company must have at least one EmployerContact!", error);
        assertEquals(companyService.getAllCompanies().size(), 0);
    }

    @Test
    public void testUpdateCompany() {
        String name = "Facebook";
        EmployerContact e = createTestEmployerContact();
        List<EmployerContact> employees = new ArrayList<EmployerContact>();
        employees.add(e);
        
        Company c = null;
        try {
        	companyService.createCompany(name, employees);
        	c = companyService.getCompany("Facebook");
        	
        	// Add new employee
            employees = c.getEmployees();
            EmployerContact e2 = createTestEmployerContact();
            e2.setEmail("anotheremail@gmail.com");
            employees.add(e2);
            
        	companyService.updateCompany(c, "Index Exchange", employees);
        	c = companyService.getCompany("Index Exchange");
        } catch (IllegalArgumentException _e) {
        	fail();
        }
        
        assertEquals("Index Exchange", c.getName());
        assertEquals(2, c.getEmployees().size());
    }

    @Test
    public void testUpdateCompanyInvalid() {
        String name = "Facebook";
        EmployerContact e = createTestEmployerContact();
        List<EmployerContact> employees = new ArrayList<EmployerContact>();
        employees.add(e);
        
        Company c = null;
        try {
        	companyService.createCompany(name, employees);
        	c = companyService.getCompany("Facebook");
        } catch (IllegalArgumentException _e) {
        	fail();
        }
        
        String error = "";
        try {
        	companyService.updateCompany(c, "", new ArrayList<EmployerContact>());
        } catch (IllegalArgumentException err) {
        	error = err.getMessage();
        }
        
        assertEquals("Company name cannot be empty! Company must have at least one EmployerContact!", error);
        
        // Original Company should still exist
        assertEquals(1, companyService.getAllCompanies().size());
        try {
        	companyService.getCompany("Facebook");
        } catch (IllegalArgumentException _e) {
        	fail();
        }
    }

    @Test
    public void testDeleteCompany() {
        String name = "Facebook";
        EmployerContact e = createTestEmployerContact();
        List<EmployerContact> employees = new ArrayList<EmployerContact>();
        employees.add(e);
        
        Company c = null;
        try {
        	companyService.createCompany(name, employees);
        	c = companyService.getCompany("Facebook");
        	companyService.deleteCompany(c);
        } catch (IllegalArgumentException _e) {
        	fail();
        }
        
        assertEquals(0, companyService.getAllCompanies().size());
    }
    
    @Test
    public void testDeleteCompanyInvalid() {
    	String error = "";
        try {
        	companyService.deleteCompany(null);
        } catch (IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Company to delete cannot be null!", error);   
    }
    
    private static EmployerContact createTestEmployerContact() {
    	EmployerContact e = new EmployerContact();
    	e.setEmail("albertkragl@fb.com");
    	e.setFirstName("Albert");
    	e.setFirstName("Kragl");
    	e.setPhoneNumber("+17781234567");
    	
    	return e;
    }
}
