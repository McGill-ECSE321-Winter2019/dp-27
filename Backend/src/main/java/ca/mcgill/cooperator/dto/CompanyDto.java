package ca.mcgill.cooperator.dto;

import java.util.List;

public class CompanyDto {

    private int id;
    private String name;
    private String city;
    private String region;
    private String country;

    private List<EmployerContactDto> employees;
    
    public CompanyDto() {}

    public CompanyDto(int id, String name, String city, String region, String country, List<EmployerContactDto> employees) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.region = region;
        this.country = country;
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
