package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.CourseOffering;
import java.util.List;

public class CourseDto {

    private int id;
    private String name;

    private List<CourseOffering> courseOfferings;

    public CourseDto(int id, String name, List<CourseOffering> courseOfferings) {
        this.id = id;
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
