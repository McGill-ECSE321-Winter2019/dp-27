package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Company {
    @Id @GeneratedValue private int id;
    private String name;

    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<EmployerContact> employees;

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
        if (this.employees == null) {
            this.employees = employees;
        } else {
            this.employees.clear();
            this.employees.addAll(employees);
        }
    }
}
