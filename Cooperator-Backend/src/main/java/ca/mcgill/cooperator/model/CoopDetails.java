package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CoopDetails {
	@Id
	private int id;
	private Company company;
	
	@ManyToOne(optional = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
    private Coop coop;
    
    @OneToOne(mappedBy = "coop")
	public Coop getCoop() {
		return this.coop;
	}

	public void setCoop(Coop coop) {
		this.coop = coop;
	}
    
    private double payPerHour;
    private int hoursPerWeek;
}