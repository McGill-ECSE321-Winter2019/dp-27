package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Report;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployerContactService extends BaseService {

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;

    /**
     * Creates an EmployerContact with a name and email
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param company
     * @return the newly created EmployerContact
     */
    @Transactional
    public EmployerContact createEmployerContact(
            String firstName, String lastName, String email, String phoneNumber, Company company) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("Employer Contact first name cannot be empty! ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Employer Contact last name cannot be empty! ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Employer Contact email cannot be empty! ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Employer Contact email must be a valid email! ");
        }
        if (phoneNumber == null || phoneNumber.trim().length() == 0) {
            error.append("Employer Contact phone number cannot be empty! ");
        } else if (!ServiceUtils.isValidPhoneNumber(phoneNumber)) {
            error.append("Employer Contact phone number must be a valid number! ");
        }
        if (company == null) {
            error.append("Employer Contact company cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        EmployerContact ec = new EmployerContact();
        ec.setFirstName(firstName.trim());
        ec.setLastName(lastName.trim());
        ec.setEmail(email.trim());
        ec.setPhoneNumber(phoneNumber.trim());
        ec.setReports(new HashSet<Report>());
        ec.setCoopDetails(new HashSet<CoopDetails>());
        ec.setCompany(company);

        return employerContactRepository.save(ec);
    }

    /**
     * Returns the EmployerContact with specified ID
     *
     * @param id
     * @return EmployerContact, if it exists
     */
    @Transactional
    public EmployerContact getEmployerContact(int id) {
        EmployerContact ec = employerContactRepository.findById(id).orElse(null);
        if (ec == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer Contact with ID " + id + " does not exist!");
        }

        return ec;
    }

    /**
     * Returns the EmployerContact with specified email
     *
     * @param email
     * @return EmployerContact, if it exists
     */
    @Transactional
    public EmployerContact getEmployerContact(String email) {
        EmployerContact ec = employerContactRepository.findByEmail(email.trim());
        if (ec == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX
                            + "Employer Contact with email "
                            + email.trim()
                            + " does not exist!");
        }

        return ec;
    }

    /**
     * Returns all EmployerContacts that exist
     *
     * @return all EmployerContacts
     */
    @Transactional
    public List<EmployerContact> getAllEmployerContacts() {
        return ServiceUtils.toList(employerContactRepository.findAll());
    }

    /**
     * Updates the specified EmployerContact
     *
     * @param ec
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param company
     * @param employerReports
     * @param coopDetails
     * @return the updated EmployerContact
     */
    @Transactional
    public EmployerContact updateEmployerContact(
            EmployerContact ec,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Company company,
            Set<Report> reports,
            Set<CoopDetails> coopDetails) {

        StringBuilder error = new StringBuilder();
        if (ec == null) {
            error.append("Employer Contact to update cannot be null! ");
        }
        if (firstName != null && firstName.trim().length() == 0) {
            error.append("Employer Contact first name cannot be empty! ");
        }
        if (lastName != null && lastName.trim().length() == 0) {
            error.append("Employer Contact last name cannot be empty! ");
        }
        if (email != null && email.trim().length() == 0) {
            error.append("Employer Contact email cannot be empty! ");
        } else if (email != null && !ServiceUtils.isValidEmail(email)) {
            error.append("Employer Contact email must be a valid email! ");
        }
        if (phoneNumber != null && phoneNumber.trim().length() == 0) {
            error.append("Employer Contact phone number cannot be empty! ");
        } else if (phoneNumber != null && !ServiceUtils.isValidPhoneNumber(phoneNumber)) {
            error.append("Employer Contact phone number must be a valid number! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (firstName != null) {
            ec.setFirstName(firstName.trim());
        }
        if (lastName != null) {
            ec.setLastName(lastName.trim());
        }
        if (email != null) {
            ec.setEmail(email.trim());
        }
        if (phoneNumber != null) {
            ec.setPhoneNumber(phoneNumber.trim());
        }
        if (reports != null) {
            ec.setReports(reports);
        }
        if (coopDetails != null) {
            ec.setCoopDetails(coopDetails);
        }
        if (company != null) {
            ec.setCompany(company);
        }

        return employerContactRepository.save(ec);
    }

    /**
     * Deletes the specified EmployerContact
     *
     * @param ec
     * @return the deleted EmployerContact
     */
    @Transactional
    public EmployerContact deleteEmployerContact(EmployerContact ec) {
        if (ec == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer Contact to delete cannot be null!");
        }

        Company c = ec.getCompany();

        List<EmployerContact> employers = c.getEmployees();
        employers.remove(ec);
        c.setEmployees(employers);

        companyRepository.save(c);

        employerContactRepository.delete(ec);

        return ec;
    }
}
