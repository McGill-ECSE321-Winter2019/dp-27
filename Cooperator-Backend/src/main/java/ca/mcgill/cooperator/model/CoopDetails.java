package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CoopDetails {
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne(optional = false)
	private EmployerContact employerContact;

	public EmployerContact getEmployerContact() {
		return this.employerContact;
	}

	public void setEmployerContact(EmployerContact employerContact) {
		this.employerContact = employerContact;
	}

	@OneToOne
	private Coop coop;

	public Coop getCoop() {
		return this.coop;
	}

	public void setCoop(Coop coop) {
		this.coop = coop;
	}

	private double payPerHour;
	private int hoursPerWeek;
}