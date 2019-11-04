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

    @OneToMany(mappedBy = "courseOffering")
    private List<Coop> coops;
    
    /*--- Constructors ---*/

    public CourseOffering(int year, Season season, Course course, List<Coop> coops) {
    	this.year = year;
    	this.season = season;
    	this.course = course;
    	this.coops = coops;
    }
    
    /*--- Getters and Setters ---*/
    
    public int getId() {
    	return this.id;
    }
    
    public int getYear() {
    	return this.year;
    }
    
    public void setYear(int year) {
    	this.year = year;
    }
    
    public Season getSeason() {
    	return this.season;
    }
    
    public void setSeason(Season season) {
    	this.season = season;
    }
    
    public Course getCourse() {
    	return this.course;
    }
    
    public void setCourse(Course course) {
    	this.course = course;
    }
    
    public List<Coop> getCoops() {
    	return this.coops;
    }
    
    public void setCoops(List<Coop> coops) {
    	this.coops = coops;
    }
    
}
