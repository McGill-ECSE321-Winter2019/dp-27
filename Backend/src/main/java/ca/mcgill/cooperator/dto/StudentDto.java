package ca.mcgill.cooperator.dto;

import java.util.Set;

public class StudentDto extends AuthorDto{
	
    private String studentId;

    private Set<CoopDto> coops;
    private Set<NotificationDto> studentReceived;

    public StudentDto() {}

    public StudentDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String studentId,
            Set<CoopDto> coops,
            Set<NotificationDto> studentReceived) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentId = studentId;
        this.coops = coops;
        this.studentReceived = studentReceived;
    }

    /*--- Getters and Setters ---*/

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Set<CoopDto> getCoops() {
        return this.coops;
    }

    public void setCoops(Set<CoopDto> coops) {
        this.coops = coops;
    }

    public Set<NotificationDto> getNotifications() {
        return this.studentReceived;
    }

    public void setNotifications(Set<NotificationDto> notifications) {
        this.studentReceived = notifications;
    }
}
