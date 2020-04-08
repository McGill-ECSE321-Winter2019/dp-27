package ca.mcgill.cooperator.dto;

import java.util.Set;

public class AuthorDto {
	
    protected Integer id;
    protected String firstName;
    protected String lastName;
    protected String email;
    private Set<ReportDto> reports;
    
    public AuthorDto() {}
    
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
    
    public Set<ReportDto> getReports() {
        return this.reports;
    }

    public void setReports(Set<ReportDto> reports) {
        this.reports = reports;
    }

}
