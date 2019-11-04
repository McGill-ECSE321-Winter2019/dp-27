package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ReportSection {
	@Id
	@GeneratedValue
	private int id;
	private String title;
	private String content;

	@ManyToOne(optional = true)
	private StudentReport stuReport;

	public StudentReport getStudentReport() {
		return this.stuReport;
	}

	public void setStudentReport(StudentReport stuReport) {
		this.stuReport = stuReport;
	}

	@ManyToOne(optional = true)
	private EmployerReport emplReport;

	public EmployerReport getEmployerReport() {
		return this.emplReport;
	}

	public void setEmployerReport(EmployerReport emplReport) {
		this.emplReport = emplReport;
	}

}
