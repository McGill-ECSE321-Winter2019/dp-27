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
    @Id @GeneratedValue private int id;
    private CoopStatus status;

    @ManyToOne(optional = false)
    private CourseOffering courseOffering;

    @OneToOne(mappedBy = "coop", optional = true)
    private CoopDetails details;

    @ManyToOne(optional = false)
    private Student student;

    @OneToMany(mappedBy = "coop")
    private List<StudentReport> studentReports;

    @OneToMany(mappedBy = "coop")
    private List<EmployerReport> employerReports;

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
