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
     * @param city
     * @param region
     * @param country
     * @param employees
     * @return the newly created Company
     */
    @Transactional
    public Company createCompany(
            String name,
            String city,
            String region,
            String country,
            List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        if (city == null || city.trim().length() == 0) {
            error.append("Company city cannot be empty! ");
        }
        if (region == null || region.trim().length() == 0) {
            error.append("Company region cannot be empty! ");
        }
        if (country == null || country.trim().length() == 0) {
            error.append("Company country cannot be empty! ");
        }
        // employees cannot be null but can be empty
        if (employees == null) {
            error.append("Company employees cannot be null! ");
        }
        if (companyExists(name, city, region, country)) {
            error.append("Company with this name and location already exists!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Company c = new Company();
        c.setName(name.trim());
        c.setCity(city);
        c.setRegion(region);
        c.setCountry(country);
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
     * Returns the Company with specified name and location
     *
     * @param name
     * @param city
     * @param region
     * @param country
     * @return Company, if it exists
     */
    @Transactional
    public Company getCompany(String name, String city, String region, String country) {
        Company c =
                companyRepository.findByNameAndCityAndRegionAndCountry(
                        name.trim(), city.trim(), region.trim(), country.trim());
        if (c == null) {
            String location = city.trim() + ", " + region.trim() + " " + country.trim();
            throw new IllegalArgumentException(
                    "Company with name "
                            + name.trim()
                            + " and location "
                            + location
                            + " does not exist!");
        }

        return c;
    }

    /**
     * Returns all Company offices with specified name
     *
     * @param name
     * @return list of Companies
     */
    @Transactional
    public List<Company> getCompanies(String name) {
        List<Company> companies = companyRepository.findByName(name.trim());

        if (companies == null || companies.isEmpty()) {
            throw new IllegalArgumentException(
                    "No offices for company with name " + name.trim() + "!");
        }

        return companies;
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
    public Company updateCompany(
            Company c,
            String name,
            String city,
            String region,
            String country,
            List<EmployerContact> employees) {
        StringBuilder error = new StringBuilder();
        if (c == null) {
            error.append("Company to update cannot be null! ");
        }
        if (name == null || name.trim().length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        if (city == null || city.trim().length() == 0) {
            error.append("Company city cannot be empty! ");
        }
        if (region == null || region.trim().length() == 0) {
            error.append("Company region cannot be empty! ");
        }
        if (country == null || country.trim().length() == 0) {
            error.append("Company country cannot be empty! ");
        }
        // employees cannot be null but can be empty
        if (employees == null) {
            error.append("Company employees cannot be null! ");
        }
        if (companyExists(name, city, region, country)) {
            error.append("Company with this name and location already exists!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        c.setName(name.trim());
        c.setCity(city);
        c.setRegion(region);
        c.setCountry(country);
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

    private boolean companyExists(String name, String city, String region, String country) {
        return companyRepository.findByNameAndCityAndRegionAndCountry(name, city, region, country)
                != null;
    }
}
