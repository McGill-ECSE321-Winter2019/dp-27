package ca.mcgill.cooperator.dto;

import java.util.List;

public class AuthorDto {

    protected Integer id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected List<ReportDto> reports;

    public AuthorDto() {}

    public AuthorDto(
            Integer id, String firstName, String lastName, String email, List<ReportDto> reports) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.reports = reports;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ReportDto> getReports() {
        return this.reports;
    }

    public void setReports(List<ReportDto> reports) {
        this.reports = reports;
    }
}
