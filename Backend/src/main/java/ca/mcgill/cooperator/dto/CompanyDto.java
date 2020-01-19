package ca.mcgill.cooperator.dto;

import java.util.List;

public class CompanyDto {

    private int id;
    private String name;

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

    public List<EmployerContactDto> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<EmployerContactDto> employees) {
        this.employees = employees;
    }
}
