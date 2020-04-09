package ca.mcgill.cooperator.dto;

import java.util.List;

public class AdminDto extends AuthorDto {

    private List<NotificationDto> sent;

    public AdminDto() {};
    
    public AdminDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            List<NotificationDto> sent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sent = sent;
    }

    public List<NotificationDto> getSentNotifications() {
        return this.sent;
    }

    public void setSentNotifications(List<NotificationDto> sent) {
        this.sent = sent;
    }
}
