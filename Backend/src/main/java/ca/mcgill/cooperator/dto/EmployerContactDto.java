package ca.mcgill.cooperator.dto;

import java.util.List;

public class EmployerContactDto {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private CompanyDto company;
    private List<CoopDetailsDto> coopDetails;
    private List<EmployerReportDto> employerReports;
    
    public EmployerContactDto() {}

    public EmployerContactDto(
            int id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            CompanyDto company,
            List<CoopDetailsDto> coopDetails,
            List<EmployerReportDto> employerReports) {
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

    public CompanyDto getCompany() {
        return this.company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public List<CoopDetailsDto> getCoopDetails() {
        return this.coopDetails;
    }

    public void setCoopDetails(List<CoopDetailsDto> coopDetails) {
        this.coopDetails = coopDetails;
    }

    public List<EmployerReportDto> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReportDto> employerReports) {
        this.employerReports = employerReports;
    }
}
