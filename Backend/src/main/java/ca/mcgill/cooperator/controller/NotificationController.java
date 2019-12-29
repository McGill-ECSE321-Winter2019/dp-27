package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.model.Notification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("notifications")
public class NotificationController {

    @GetMapping("/{id}")
    public String getNotificationById(@PathVariable int id) {
        return "Hello World";
    }

    private NotificationDto convertToDto(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException("Notification does not exist!");
        }
        return new NotificationDto(
                n.getId(), n.getTitle(), n.getBody(), n.getStudent(), n.getSender());
    }
}
