package ca.mcgill.cooperator.dto;

import java.util.List;

public class CourseDto {

    private Integer id;
    private String name;

    private List<CourseOfferingDto> courseOfferings;

    public CourseDto() {}

    public CourseDto(Integer id, String name, List<CourseOfferingDto> courseOfferings) {
        this.id = id;
        this.name = name;
        this.courseOfferings = courseOfferings;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseOfferingDto> getCourseOfferings() {
        return this.courseOfferings;
    }

    public void setCourseOfferings(List<CourseOfferingDto> courseOfferings) {
        this.courseOfferings = courseOfferings;
    }
}
