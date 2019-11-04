package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Course {
    @Id @GeneratedValue private int id;
    private String name;

    @OneToMany(mappedBy = "course")
    private List<CourseOffering> courseOfferings;

    public List<CourseOffering> getCourseOfferings() {
        return this.courseOfferings;
    }

    public void setCourseOfferings(List<CourseOffering> courseOfferings) {
        this.courseOfferings = courseOfferings;
    }
}
