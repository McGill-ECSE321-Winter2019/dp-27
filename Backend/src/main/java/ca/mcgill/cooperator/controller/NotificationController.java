package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.ArrayList;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("notifications")
public class NotificationController extends BaseController {

    @Autowired NotificationService notificationService;
    @Autowired StudentService studentService;
    @Autowired AdminService adminService;

    /**
     * Creates a new Notification
     *
     * <p>In request body:
     *
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return the created Notification
     */
    @PostMapping("")
    public NotificationDto createNotification(@RequestBody NotificationDto n) {

        String title = n.getTitle();
        String body = n.getBody();

        StudentDto studentDto = n.getStudent();
        Student student = null;
        if (studentDto != null) {
            student = studentService.getStudentById(studentDto.getId());
        }

        AdminDto adminDto = n.getSender();
        Admin sender = null;
        if (adminDto != null) {
            sender = adminService.getAdmin(adminDto.getId());
        }

        Notification notification =
                notificationService.createNotification(title, body, student, sender);

        return ControllerUtils.convertToDto(notification);
    }

    /**
     * Creates a new Notification for all the students in the list
     *
     *
     * @param title
     * @param body
     * @param list of student Ids
     * @param sender Id
     * @return list of the created Notifications
     */
    @PostMapping("/many")
    public List<NotificationDto> createManyNotifications(
            @RequestParam("studentIds") List<Integer> stuIds,
            @RequestParam("admin") Integer adminId,
            @RequestParam("title") String title,
            @RequestParam("body") String body) {

        Admin sender = null;
        if (adminId != null) {
            sender = adminService.getAdmin(adminId);
        }

        Student student = null;
        List<Notification> notifs = new ArrayList<>();
        for (Integer id : stuIds) {
            student = studentService.getStudentById(id);
            Notification notification =
                    notificationService.createNotification(title, body, student, sender);
            notifs.add(notification);
        }

        return ControllerUtils.convertNotifListToDto(notifs);
    }

    /**
     * Gets a Notification by ID
     *
     * @param id
     * @return NotificationDto object
     */
    @GetMapping("/{id}")
    public NotificationDto getNotificationById(@PathVariable int id) {
        Notification n = notificationService.getNotification(id);

        return ControllerUtils.convertToDto(n);
    }

    /**
     * Sets Notification seen by id
     *
     * @param id
     * @return list of NotificationDtos
     */
    @PutMapping("/{id}/mark-as-read")
    public List<NotificationDto> setNotificationsSeen(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        List<Notification> all = notificationService.markAllAsRead(student);
        return ControllerUtils.convertNotifListToDto(all);
    }

    /**
     * Gets all Notifications
     *
     * @return list of NotificationDtos
     */
    @GetMapping("")
    public List<NotificationDto> getAllNotifications() {
        List<Notification> n = notificationService.getAllNotifications();

        return ControllerUtils.convertNotifListToDto(n);
    }

    /**
     * Gets unseen Notifications by Student
     *
     * @return list of NotificationDtos
     */
    @GetMapping("/{id}/unread")
    public List<NotificationDto> getUnreadForStudent(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        List<Notification> n = notificationService.getUnreadForStudent(student);

        return ControllerUtils.convertNotifListToDto(n);
    }

    /**
     * Gets all Notifications for Student
     *
     * @return list of NotificationDtos
     */
    @GetMapping("/student/{id}")
    public List<NotificationDto> getAllForStudent(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        List<Notification> n = notificationService.getAllNotificationsOfStudent(student);

        return ControllerUtils.convertNotifListToDto(n);
    }

    /**
     * Updates an existing Notification
     *
     * @param id
     *     <p>In request body:
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return the updated Notification
     */
    @PutMapping("/{id}")
    public NotificationDto updateNotification(
            @PathVariable int id, @RequestBody NotificationDto n) {
        Notification notification = notificationService.getNotification(id);

        StudentDto studentDto = n.getStudent();
        Student student = null;
        if (studentDto != null) {
            student = studentService.getStudentById(studentDto.getId());
        }

        AdminDto adminDto = n.getSender();
        Admin sender = null;
        if (adminDto != null) {
            sender = adminService.getAdmin(adminDto.getId());
        }

        Notification notifs =
                notificationService.updateNotification(
                        notification, n.getTitle(), n.getBody(), student, sender);

        return ControllerUtils.convertToDto(notifs);
    }

    /**
     * Deletes an existing Notification
     *
     * @param id
     * @return the deleted Notification
     */
    @DeleteMapping("/{id}")
    public NotificationDto deleteNotification(@PathVariable int id) {
        Notification n =
                notificationService.deleteNotification(notificationService.getNotification(id));

        return ControllerUtils.convertToDto(n);
    }
}
