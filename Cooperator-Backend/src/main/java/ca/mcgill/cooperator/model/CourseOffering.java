package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class CourseOffering {
	@Id
	private int id;
    private int year;
    private Season season;
    
    private Course course;
    
	@ManyToOne(optional = false)
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	private List<Coop> coops;
    
    @OneToMany(mappedBy = "coops")
	public List<Coop> getCoops() {
		return this.coops;
	}

	public void setCoop(List<Coop> coops) {
		this.coops = coops;
	}
	
	
	
}
