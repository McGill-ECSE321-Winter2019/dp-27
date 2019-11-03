package ca.mcgill.cooperator.model;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Course {
	@Id @GeneratedValue
	private int id;
    //not really sure about this
	private String name;
	
	private List<CourseOffering> courseOfferings;
    
    @OneToMany(mappedBy = "course")
	public List<CourseOffering> getCourseOfferings() {
		return this.courseOfferings;
	}

	public void setCourseOfferings(List<CourseOffering> courseOfferings) {
		this.courseOfferings = courseOfferings;
	}

}