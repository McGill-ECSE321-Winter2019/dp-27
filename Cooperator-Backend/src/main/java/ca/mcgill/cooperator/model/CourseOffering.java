package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CourseOffering {
    @Id @GeneratedValue private int id;
    private int year;
    private Season season;

    @ManyToOne(optional = false)
    private Course course;

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @OneToMany(mappedBy = "courseOffering")
    private List<Coop> coops;

    public List<Coop> getCoops() {
        return this.coops;
    }

    public void setCoop(List<Coop> coops) {
        this.coops = coops;
    }
}
