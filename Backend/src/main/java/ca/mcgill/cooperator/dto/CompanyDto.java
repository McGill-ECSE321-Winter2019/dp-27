package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.EmployerContact;
import java.util.List;

public class CompanyDto {

    private int id;
    private String name;

    private List<EmployerContact> employees;

    public CompanyDto(int id, String name, List<EmployerContact> employees) {
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

    public List<EmployerContact> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<EmployerContact> employees) {
        this.employees = employees;
    }
}
