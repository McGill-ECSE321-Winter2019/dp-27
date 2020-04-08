package ca.mcgill.cooperator.dto;

import java.util.Set;

public class AdminDto extends AuthorDto {

    private Set<NotificationDto> sent;

    public AdminDto(
            Integer id,
            String firstName,
            String lastName,
            String email,
            Set<NotificationDto> sent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sent = sent;
    }

    public Set<NotificationDto> getSentNotifications() {
        return this.sent;
    }

    public void setSentNotifications(Set<NotificationDto> sent) {
        this.sent = sent;
    }
}
