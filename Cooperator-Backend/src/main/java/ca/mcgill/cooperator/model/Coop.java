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
    private int year;
    private CoopStatus status;
    private Season term;

    @ManyToOne(optional = false)
    private CourseOffering courseOffering;

    public CourseOffering getCourseOffering() {
        return this.courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    @OneToOne(mappedBy = "coop", optional = true)
    private CoopDetails details;

    public CoopDetails getCoop() {
        return this.details;
    }

    public void setCoopDetails(CoopDetails details) {
        this.details = details;
    }

    @ManyToOne(optional = false)
    private Student student;

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @OneToMany(mappedBy = "coop")
    private List<StudentReport> studentReports;

    public List<StudentReport> getStudentReport() {
        return this.studentReports;
    }

    public void setStudentReports(List<StudentReport> studentReports) {
        this.studentReports = studentReports;
    }

    @OneToMany(mappedBy = "coop")
    private List<EmployerReport> employerReports;

    public List<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReport> employerReports) {
        this.employerReports = employerReports;
    }
}
