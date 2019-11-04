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
    
    /*--- Constructors ---*/

    public Course(String name, List<CourseOffering> courseOfferings) {
    	this.name = name;
    	this.courseOfferings = courseOfferings;
    }
    
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
    	this.courseOfferings = courseOfferings;
    }
}
