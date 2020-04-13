package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Season;
import java.util.List;

public class CourseOfferingDto {

    private Integer id;
    private Integer year;
    private Season season;

    private CourseDto course;
    private List<CoopDto> coops;
    private List<ReportConfigDto> reportConfigs;

    public CourseOfferingDto() {}

    public CourseOfferingDto(
            Integer id, Integer year, Season season, CourseDto course, List<CoopDto> coops) {
        this.id = id;
        this.year = year;
        this.season = season;
        this.course = course;
        this.coops = coops;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
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

    public List<CoopDto> getCoops() {
        return this.coops;
    }

    public void setCoops(List<CoopDto> coops) {
        this.coops = coops;
    }
    
    public List<ReportConfigDto> getReportConfigs() {
        return this.reportConfigs;
    }

    public void setReportConfigs(List<ReportConfigDto> reportConfigs) {
        this.reportConfigs = reportConfigs;
    }
}
