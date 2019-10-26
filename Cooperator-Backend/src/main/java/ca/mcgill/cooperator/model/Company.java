package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.OneToMany;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {
	@Id
	private int id;
    private String name;
    
    private List<EmployerContact> employees;
    
    @OneToMany(mappedBy = "employees")
	public List<EmployerContact> getCoops() {
		return this.employees;
	}

	public void setCoop(List<EmployerContact> employees) {
		this.employees = employees;
	}
}