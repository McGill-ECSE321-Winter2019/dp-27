package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Course {
    @Id @GeneratedValue private int id;
    private String name;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CourseOffering> courseOfferings;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseOffering> getCourseOfferings() {
        return this.courseOfferings;
    }

    public void setCourseOfferings(List<CourseOffering> courseOfferings) {
    	if(this.courseOfferings == null)
    		this.courseOfferings = courseOfferings;
    	else {
    		this.courseOfferings.clear();
    		this.courseOfferings.addAll(courseOfferings);
    	}
    }
}
