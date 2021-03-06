package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.CoopStatus;
import java.util.List;

public class CoopDto {

    private Integer id;
    private CoopStatus status;

    private CourseOfferingDto courseOffering;
    private CoopDetailsDto details;
    private StudentDto student;
    private List<ReportDto> reports;

    public CoopDto() {}

    public CoopDto(
            Integer id,
            CoopStatus status,
            CourseOfferingDto courseOffering,
            CoopDetailsDto details,
            StudentDto student,
            List<ReportDto> reports) {
        this.id = id;
        this.status = status;
        this.courseOffering = courseOffering;
        this.details = details;
        this.student = student;
        this.reports = reports;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public CoopStatus getStatus() {
        return this.status;
    }

    public void setStatus(CoopStatus status) {
        this.status = status;
    }

    public CourseOfferingDto getCourseOffering() {
        return this.courseOffering;
    }

    public void setCourseOffering(CourseOfferingDto courseOffering) {
        this.courseOffering = courseOffering;
    }

    public CoopDetailsDto getCoopDetails() {
        return this.details;
    }

    public void setCoopDetails(CoopDetailsDto details) {
        this.details = details;
    }

    public StudentDto getStudent() {
        return this.student;
    }

    public void setStudent(StudentDto student) {
        this.student = student;
    }

    public List<ReportDto> getReports() {
        return this.reports;
    }

    public void setReports(List<ReportDto> reports) {
        this.reports = reports;
    }
}
