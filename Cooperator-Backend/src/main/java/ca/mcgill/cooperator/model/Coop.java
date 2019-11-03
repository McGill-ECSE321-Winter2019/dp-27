package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Coop {
	@Id
	@GeneratedValue
	private int id;
	private int year;
	private CoopStatus status;
	private Season term;

	private CourseOffering courseOffering;

	@ManyToOne(optional = false)
	public CourseOffering getCourseOffering() {
		return this.courseOffering;
	}

	public void setCourseOffering(CourseOffering courseOffering) {
		this.courseOffering = courseOffering;
	}

	private CoopDetails details;

	@OneToOne(mappedBy = "coop", optional = true)
	public CoopDetails getCoop() {
		return this.details;
	}

	public void setCoopDetails(CoopDetails details) {
		this.details = details;
	}

	private Student student;

	@ManyToOne(optional = false)
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	private List<StudentReport> studentReports;

	@OneToMany(mappedBy = "studentReports")
	public List<StudentReport> getStudentReport() {
		return this.studentReports;
	}

	public void setStudentReports(List<StudentReport> studentReports) {
		this.studentReports = studentReports;
	}

	private List<EmployerReport> employerReports;

	@OneToMany(mappedBy = "studentReports")
	public List<EmployerReport> getEmployerReports() {
		return this.employerReports;
	}

	public void setEmployerReports(List<EmployerReport> employerReports) {
		this.employerReports = employerReports;
	}
}