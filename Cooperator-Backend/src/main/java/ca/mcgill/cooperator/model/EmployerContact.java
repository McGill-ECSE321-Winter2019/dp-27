package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class EmployerContact {
	@Id
	@GeneratedValue
	private int id;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;

	private Company company;

	@ManyToOne
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	private List<CoopDetails> coopdetails;

	@OneToMany(mappedBy = "coopdetails")
	public List<CoopDetails> getCoopDetails() {
		return this.coopdetails;
	}

	public void setCoop(List<CoopDetails> coopdetails) {
		this.coopdetails = coopdetails;
	}

	private List<EmployerReport> employerReports;

	@OneToMany(mappedBy = "employerReports")
	public List<EmployerReport> getEmployerReports() {
		return this.employerReports;
	}

	public void setEmployerReports(List<EmployerReport> employerReports) {
		this.employerReports = employerReports;
	}

}
