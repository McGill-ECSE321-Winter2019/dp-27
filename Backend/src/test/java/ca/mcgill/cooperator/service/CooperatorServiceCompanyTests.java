package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
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
public class CooperatorServiceCompanyTests {

    @Autowired CompanyService companyService;
    @Autowired EmployerContactService employerContactService;

    @Autowired CompanyRepository companyRepository;
    @Autowired EmployerContactRepository employerContactRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        companyRepository.deleteAll();
        employerContactRepository.deleteAll();
    }

    @Test
    public void testCreateCompany() {
        String name = "Facebook";

        Company c = null;
        try {
            c = companyService.createCompany(name, new ArrayList<EmployerContact>());

            companyService.getCompany("Facebook");
            companyService.getCompany(c.getId());
        } catch (IllegalArgumentException _e) {
            fail();
        }

        assertEquals(companyService.getAllCompanies().size(), 1);
    }

    @Test
    public void testCreateCompanyNull() {
        String error = "";
        try {
            companyService.createCompany(null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Company name cannot be empty! " + "Company employees cannot be null!", error);
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

        assertEquals("Company name cannot be empty! " + "Company employees cannot be null!", error);
        assertEquals(companyService.getAllCompanies().size(), 0);
    }

    @Test
    public void testUpdateCompany() {
        String name = "Facebook";
        List<EmployerContact> employees = new ArrayList<EmployerContact>();

        EmployerContact ec = null;
        Company c = null;
        try {
            companyService.createCompany(name, employees);
            c = companyService.getCompany("Facebook");

            // Add new employee
            ec = createTestEmployerContact(c);
            employees.add(ec);

            companyService.updateCompany(c, "Index Exchange", employees);
            c = companyService.getCompany("Index Exchange");
        } catch (IllegalArgumentException _e) {
            fail();
        }

        assertEquals("Index Exchange", c.getName());
        assertEquals(1, c.getEmployees().size());
    }

    @Test
    public void testUpdateCompanyInvalid() {
        String name = "Facebook";
        List<EmployerContact> employees = new ArrayList<EmployerContact>();

        Company c = null;
        try {
            companyService.createCompany(name, employees);
            c = companyService.getCompany("Facebook");
        } catch (IllegalArgumentException _e) {
            fail();
        }

        String error = "";
        try {
            companyService.updateCompany(c, "", null);
        } catch (IllegalArgumentException err) {
            error = err.getMessage();
        }

        assertEquals("Company name cannot be empty! " + "Company employees cannot be null!", error);

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
        List<EmployerContact> employees = new ArrayList<EmployerContact>();

        EmployerContact ec = null;
        Company c = null;

        try {
            c = companyService.createCompany(name, employees);
            ec = createTestEmployerContact(c);
            employees.add(ec);
            c = companyService.updateCompany(c, name, employees);
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

    private EmployerContact createTestEmployerContact(Company c) {
        EmployerContact e = new EmployerContact();

        e =
                employerContactService.createEmployerContact(
                        "Albert", "Kragl", "albert@kragl.com", "123456678", c);

        return e;
    }
}
