package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerReport;
import java.util.List;

public class EmployerContactDto {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private Company company;
    private List<CoopDetails> coopDetails;
    private List<EmployerReport> employerReports;

    public EmployerContactDto(
            int id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            Company company,
            List<CoopDetails> coopDetails,
            List<EmployerReport> employerReports) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.coopDetails = coopDetails;
        this.employerReports = employerReports;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<CoopDetails> getCoopDetails() {
        return this.coopDetails;
    }

    public void setCoopDetails(List<CoopDetails> coopDetails) {
        this.coopDetails = coopDetails;
    }

    public List<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReport> employerReports) {
        this.employerReports = employerReports;
    }
}
