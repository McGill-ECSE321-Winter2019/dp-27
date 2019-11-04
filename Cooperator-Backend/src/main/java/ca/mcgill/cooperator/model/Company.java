package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {
    @Id @GeneratedValue private int id;
    private String name;

    @OneToMany(mappedBy = "company")
    private List<EmployerContact> employees;

    /*--- Constructors ---*/

    public Company(String name, List<EmployerContact> employees) {
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
