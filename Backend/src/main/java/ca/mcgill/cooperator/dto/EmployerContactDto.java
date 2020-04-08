package ca.mcgill.cooperator.dto;

import java.util.List;

public class EmployerContactDto extends AuthorDto {

    private String phoneNumber;

    private CompanyDto company;
    private List<CoopDetailsDto> coopDetails;

    public EmployerContactDto() {}

    public EmployerContactDto(
            Integer id,
            String email,
            String firstName,
            String lastName,
            String phoneNumber,
            CompanyDto company,
            List<CoopDetailsDto> coopDetails,
            List<ReportDto> reports) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.coopDetails = coopDetails;
        this.reports = reports;
    }

    /*--- Getters and Setters ---*/

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
}
