package ca.mcgill.cooperator.dto;

import java.util.List;

public class AdminDto {

    private int id;
    private String firstName;
    private String lastName;
    private String email;

    private List<NotificationDto> sent;

    public AdminDto() {};
    
    public AdminDto(
            int id, String firstName, String lastName, String email, List<NotificationDto> sent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sent = sent;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<NotificationDto> getSentNotifications() {
        return this.sent;
    }

    public void setSentNotifications(List<NotificationDto> sent) {
        this.sent = sent;
    }
}
