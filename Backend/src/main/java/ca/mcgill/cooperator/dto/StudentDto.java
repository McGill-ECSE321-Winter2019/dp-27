package ca.mcgill.cooperator.dto;

import java.util.List;

public class StudentDto extends AuthorDto {

    private String studentId;

    private List<CoopDto> coops;
    private List<NotificationDto> studentReceived;

    public StudentDto() {}

    public StudentDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String studentId,
            List<CoopDto> coops,
            List<NotificationDto> studentReceived,
            List<ReportDto> reports) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentId = studentId;
        this.coops = coops;
        this.studentReceived = studentReceived;
        this.reports = reports;
    }

    /*--- Getters and Setters ---*/

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<CoopDto> getCoops() {
        return this.coops;
    }

    public void setCoops(List<CoopDto> coops) {
        this.coops = coops;
    }

    public List<NotificationDto> getNotifications() {
        return this.studentReceived;
    }

    public void setNotifications(List<NotificationDto> notifications) {
        this.studentReceived = notifications;
    }
}
