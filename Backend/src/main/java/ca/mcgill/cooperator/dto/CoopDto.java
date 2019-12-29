package ca.mcgill.cooperator.dto;

import java.util.List;

import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;

public class CoopDto {
	
	private int id;
	private CoopStatus status;
	
	private CourseOffering courseOffering;
	private CoopDetails details;
	private Student student;
	private List<StudentReport> studentReports;
	private List<EmployerReport> employerReports;
	
	public CoopDto(int id, CoopStatus status, CourseOffering courseOffering, CoopDetails details, Student student, List<StudentReport> studentReports, List<EmployerReport> employerReports) {
		this.id = id;
		this.status = status;
		this.courseOffering = courseOffering;
		this.details = details;
		this.student = student;
		this.studentReports = studentReports;
		this.employerReports = employerReports;
	}
	
	/*--- Getters and Setters ---*/
	
	public int getId() {
        return this.id;
    }

    public CoopStatus getStatus() {
        return this.status;
    }

    public void setStatus(CoopStatus status) {
        this.status = status;
    }

    public CourseOffering getCourseOffering() {
        return this.courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    public CoopDetails getCoopDetails() {
        return this.details;
    }

    public void setCoopDetails(CoopDetails details) {
        this.details = details;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<StudentReport> getStudentReports() {
        return this.studentReports;
    }

    public void setStudentReports(List<StudentReport> studentReports) {
        this.studentReports = studentReports;
    }

    public List<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReport> employerReports) {
        this.employerReports = employerReports;
    }

}
