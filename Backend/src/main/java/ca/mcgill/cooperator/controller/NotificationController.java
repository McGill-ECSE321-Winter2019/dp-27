package ca.mcgill.cooperator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.StudentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("notifications")
public class NotificationController {

	@Autowired NotificationService notificationService;
	@Autowired StudentService studentService;
	@Autowired AdminService adminService;
	
	/**
	 * Gets notification by id
	 * 
	 * @param id
	 * @return NotificationDto
	 */
    @GetMapping("/{id}")
    public NotificationDto getNotificationById(@RequestParam int id) {
        Notification n = notificationService.getNotification(id);
        
        return ControllerUtils.convertToDto(n);
    }
    
    /**
     * Gets Notification by title
     * 
     * @param title
     * @return NotificationDto
     */
    @GetMapping("/{title}")
    public NotificationDto getNotificationByTitle(@RequestParam String title) {
    	Notification n = notificationService.getNotification(title);
    	
    	return ControllerUtils.convertToDto(n);
    }
    
    /**
     * Gets all Notifications
     * 
     * @return List<NotificationDto>
     */
    @GetMapping("")
    public List<NotificationDto> getAllNotifications(){
    	List<Notification> n = notificationService.getAllNotifications();
    	
    	return ControllerUtils.convertNotifListToDto(n);
    }
    
    /**
     * Creates Notification
     * <p>
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return created Notification
     */
    @PostMapping("")
    public NotificationDto createNotification(@RequestBody NotificationDto n) {
    	
    	String title = n.getTitle();
    	String body = n.getBody();
    	
    	StudentDto studentDto = n.getStudent();
    	Student student = studentService.getStudentById(studentDto.getId());
    	
    	AdminDto adminDto = n.getSender();
    	Admin sender = adminService.getAdmin(adminDto.getId());
    	
    	Notification notification = notificationService.createNotification(title, body, student, sender);
    	
    	return ControllerUtils.convertToDto(notification);
    }
    /**
     * Updates notification
     * 
     * @param n
     * @return updated notification
     */
    @PutMapping("")
    public NotificationDto updateNotification(@RequestBody NotificationDto n) {
    	Notification notification = notificationService.getNotification(n.getId());
    	Admin sender = adminService.getAdmin(n.getSender().getId());
    	Student student = studentService.getStudentById(n.getStudent().getId());
    	Notification notifs = notificationService.updateNotification(notification, n.getTitle(), n.getBody(), student, sender);
    	
    	return ControllerUtils.convertToDto(notifs);
    }
    
    /**
     * Deletes Notification
     * 
     * @param id
     * @return deleted Notification
     */
    @DeleteMapping("/{id}")
    public NotificationDto deleteNotification(@PathVariable int id) {
    	Notification n = notificationService.deleteNotification(notificationService.getNotification(id));
    	
    	return ControllerUtils.convertToDto(n);
    }
}
