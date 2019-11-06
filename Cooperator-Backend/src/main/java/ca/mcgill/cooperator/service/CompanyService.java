package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired(required = true)
    CompanyRepository companyRepository;

    /**
     * Creates a new Company with specified name and employees
     *
     * @param name
     * @param employees
     * @return the newly created Company
     */
    public Company createCompany(String name, List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (name == null || name.length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        if (employees == null || employees.isEmpty()) {
            error.append("Company must have at least one EmployerContact!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString());
        }

        Company c = new Company();
        c.setName(name);
        c.setEmployees(employees);
        companyRepository.save(c);

        return c;
    }

    /**
     * Returns the Company with specified ID
     *
     * @param id
     * @return Company, if it exists
     */
    public Company getCompany(int id) {
        Company c = companyRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Company with ID " + id + " does not exist!");
        }

        return c;
    }

    /**
     * Returns the Company with specified name
     *
     * @param name
     * @return Company, if it exists
     */
    public Company getCompany(String name) {
        Company c = companyRepository.findByName(name);
        if (c == null) {
            throw new IllegalArgumentException("Company with name " + name + " does not exist!");
        }

        return c;
    }

    /**
     * Updates the specified Company
     *
     * @param c
     * @param name
     * @param employees
     * @return the updated Company
     */
    public Company updateCompany(Company c, String name, List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (c == null) {
            error.append("Company to update cannot be null! ");
        }
        if (name == null || name.length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        if (employees == null || employees.isEmpty()) {
            error.append("Company must have at least one EmployerContact!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString());
        }

        c.setName(name);
        c.setEmployees(employees);
        companyRepository.save(c);

        return c;
    }

    /**
     * Deletes the specified Company
     *
     * @param c
     * @return the deleted Company
     */
    public Company deleteCompany(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company to delete cannot be null!");
        }
        companyRepository.delete(c);

        return c;
    }
}
