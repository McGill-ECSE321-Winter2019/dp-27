package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends BaseService {

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
        if (companyExists(name, city, region, country)) {
            error.append("Company with this name and location already exists!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Company c = new Company();
        c.setName(name.trim());
        c.setCity(city);
        c.setRegion(region);
        c.setCountry(country);
        if (employees == null) {
            employees = new ArrayList<EmployerContact>();
        }
        c.setEmployees(employees);
        companyRepository.save(c);

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
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Company with ID " + id + " does not exist!");
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
                    ERROR_PREFIX
                            + "Company with name "
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
                    ERROR_PREFIX + "No offices for company with name " + name.trim() + "!");
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
        if (name != null && name.trim().length() == 0) {
            error.append("Company name cannot be empty! ");
        }
        if (city != null && city.trim().length() == 0) {
            error.append("Company city cannot be empty! ");
        }
        if (region != null && region.trim().length() == 0) {
            error.append("Company region cannot be empty! ");
        }
        if (country != null && country.trim().length() == 0) {
            error.append("Company country cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (name == null) {
            name = c.getName();
        }
        if (city == null) {
            city = c.getCity();
        }
        if (region == null) {
            region = c.getRegion();
        }
        if (country == null) {
            country = c.getCountry();
        }

        if (companyExists(name, city, region, country)) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Company with this name and location already exists!");
        }

        c.setName(name.trim());
        c.setCity(city.trim());
        c.setRegion(region.trim());
        c.setCountry(country.trim());

        if (employees != null) {
            c.setEmployees(employees);
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
            throw new IllegalArgumentException(ERROR_PREFIX + "Company to delete cannot be null!");
        }
        companyRepository.delete(c);

        return c;
    }

    private boolean companyExists(String name, String city, String region, String country) {
        return companyRepository.findByNameAndCityAndRegionAndCountry(name, city, region, country)
                != null;
    }
}
