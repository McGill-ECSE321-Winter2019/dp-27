package ca.mcgill.cooperator.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class CompanyDto {

    private int id;
    private String name;
    private String city;
    private String region;
    private String country;

    private List<EmployerContactDto> employees;

    public CompanyDto(int id, String name, List<EmployerContactDto> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<EmployerContactDto> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<EmployerContactDto> employees) {
        this.employees = employees;
    }
}
