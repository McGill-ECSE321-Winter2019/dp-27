package ca.mcgill.cooperator.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class ReportSection {
	@Id @GeneratedValue
	private int id;
	private String title;
	private String content;
	
	private StudentReport stuReport;
	
	@ManyToOne(optional = true)
	public StudentReport getStudentReport() {
		return this.stuReport;
	}

	public void setStudentReport(StudentReport stuReport) {
		this.stuReport = stuReport;
	}
	
	private EmployerReport emplReport;
	
	@ManyToOne(optional = true)
	public EmployerReport getEmployerReport() {
		return this.emplReport;
	}

	public void setEmployerReport(EmployerReport emplReport) {
		this.emplReport = emplReport;
	}

}
