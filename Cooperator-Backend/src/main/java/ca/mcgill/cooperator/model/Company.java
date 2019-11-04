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

    public List<EmployerContact> getCoops() {
        return this.employees;
    }

    public void setCoop(List<EmployerContact> employees) {
        this.employees = employees;
    }
}
