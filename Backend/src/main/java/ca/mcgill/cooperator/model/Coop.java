package ca.mcgill.cooperator.model;


import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Coop {
    @Id @GeneratedValue private int id;
    private CoopStatus status;

    @ManyToOne(optional = false)
    private CourseOffering courseOffering;

    @OneToOne(
            mappedBy = "coop",
            optional = true,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CoopDetails details;

    @ManyToOne(optional = false)
    private Student student;

    @OneToMany(
            mappedBy = "coop",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<StudentReport> studentReports;

    @OneToMany(
            mappedBy = "coop",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<EmployerReport> employerReports;

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

    public Set<StudentReport> getStudentReports() {
        return this.studentReports;
    }

    public void setStudentReports(Set<StudentReport> studentReports) {
        if (this.studentReports == null) {
            this.studentReports = studentReports;
        } else {
            this.studentReports.clear();
            this.studentReports.addAll(studentReports);
        }
    }

    public Set<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(Set<EmployerReport> employerReports) {
        if (this.employerReports == null) {
            this.employerReports = employerReports;
        } else {
            this.employerReports.clear();
            this.employerReports.addAll(employerReports);
        }
    }
}
