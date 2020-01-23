package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Season;
import java.util.List;

public class CourseOfferingDto {

    private int id;
    private int year;
    private Season season;

    private CourseDto course;

    public CourseOfferingDto(
            int id, int year, Season season, CourseDto course) {
        this.id = id;
        this.year = year;
        this.season = season;
        this.course = course;
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

    public CourseDto getCourse() {
        return this.course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }
}
