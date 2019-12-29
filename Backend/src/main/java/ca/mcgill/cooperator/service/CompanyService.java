package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired CompanyRepository companyRepository;

    @Autowired EmployerContactRepository employerContactRepository;

    /**
     * Creates a new Company with specified name and employees
     *
     * @param name
     * @param employees
     * @return the newly created Company
     */
    @Transactional
    public Company createCompany(String name, List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        // employees cannot be null but can be empty
        if (employees == null) {
            error.append("Company employees cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Company c = new Company();
        c.setName(name.trim());
        c.setEmployees(employees);
        companyRepository.save(c);

        for (EmployerContact employerContact : employees) {
            // We do this in case a new employee does not have the Company field set
            employerContact.setCompany(c);
            employerContactRepository.save(employerContact);
        }

        return companyRepository.save(c);
    }

    /**
     * Returns the Company with specified ID
     *
     * @param id
     * @return Company, if it exists
     */
    @Transactional
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
    @Transactional
    public Company getCompany(String name) {
        Company c = companyRepository.findByName(name.trim());
        if (c == null) {
            throw new IllegalArgumentException(
                    "Company with name " + name.trim() + " does not exist!");
        }

        return c;
    }

    /**
     * Returns all Companies that exist
     *
     * @return all Companies
     */
    @Transactional
    public List<Company> getAllCompanies() {
        return ServiceUtils.toList(companyRepository.findAll());
    }

    /**
     * Updates the specified Company
     *
     * @param c
     * @param name
     * @param employees
     * @return the updated Company
     */
    @Transactional
    public Company updateCompany(Company c, String name, List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (c == null) {
            error.append("Company to update cannot be null! ");
        }
        if (name == null || name.trim().length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        // employees cannot be null but can be empty
        if (employees == null) {
            error.append("Company employees cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        c.setName(name.trim());
        c.setEmployees(employees);

        companyRepository.save(c);

        for (EmployerContact employerContact : employees) {
            // We do this in case a new employee does not have the Company field set
            employerContact.setCompany(c);
            employerContactRepository.save(employerContact);
        }

        return companyRepository.save(c);
    }

    /**
     * Deletes the specified Company
     *
     * @param c
     * @return the deleted Company
     */
    @Transactional
    public Company deleteCompany(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company to delete cannot be null!");
        }
        companyRepository.delete(c);

        return c;
    }
}
