package ca.mcgill.cooperator.dto;

import java.util.List;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.Season;

public class CourseOfferingDto {
	
	private int id;
    private int year;
    private Season season;
    
    private Course course;
    private List<Coop> coops;
    
    public CourseOfferingDto(int id, int year, Season season, Course course, List<Coop> coops) {
    	this.id = id;
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
