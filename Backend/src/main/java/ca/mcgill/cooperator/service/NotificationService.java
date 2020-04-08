package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService extends BaseService {

    @Autowired NotificationRepository notificationRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired AdminRepository adminRepository;

    /**
     * Creates a new Notification
     *
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return created Notification
     */
    @Transactional
    public Notification createNotification(
            String title, String body, Student student, Admin sender) {
        StringBuilder error = new StringBuilder();
        if (title == null || title.trim().length() == 0) {
            error.append("Notification title cannot be empty! ");
        }
        if (body == null || body.trim().length() == 0) {
            error.append("Notification body cannot be empty! ");
        }
        if (student == null) {
            error.append("Notification must have a Student receiver! ");
        }
        if (sender == null) {
            error.append("Notification must have an Admin sender!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Notification n = new Notification();
        n.setTitle(title.trim());
        n.setBody(body.trim());
        n.setSender(sender);
        n.setStudent(student);
        n.setSeen(false);
        n.setTimeStamp(System.currentTimeMillis());

        return notificationRepository.save(n);
    }

    /**
     * Gets an existing Notification by ID
     *
     * @param id
     * @return Notification with specified id
     */
    @Transactional
    public Notification getNotification(int id) {
        Notification n = notificationRepository.findById(id).orElse(null);
        if (n == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Notification with ID " + id + " does not exist!");
        }

        return n;
    }

    /**
     * Gets all Notifications
     *
     * @return all Notifications
     */
    @Transactional
    public List<Notification> getAllNotifications() {
        return ServiceUtils.toList(notificationRepository.findAll());
    }

    /**
     * Gets all Notifications for the specified Student
     *
     * @return all Notifications for specified Student
     */
    @Transactional
    public List<Notification> getAllNotificationsOfStudent(Student student) {
        return ServiceUtils.toList(notificationRepository.findByStudent(student));
    }

    /**
     * Gets all unseen Notifications for the specified Student
     *
     * @return all unseen Notifications for the specified Student
     */
    @Transactional
    public List<Notification> getUnreadForStudent(Student student) {
        List<Notification> unread = new ArrayList<>();
        for (Notification n : notificationRepository.findByStudent(student)) {
            if (!n.getSeen()) unread.add(n);
        }
        return unread;
    }

    /**
     * Sets all Notifications of specified Student to seen
     *
     * @return all Notifications for specified Student
     */
    public List<Notification> markAllAsRead(Student student) {
        for (Notification n : notificationRepository.findByStudent(student)) {
            markAsRead(n);
        }
        return notificationRepository.findByStudent(student);
    }

    /**
     * Sets a Notification to seen
     *
     * @return the updated Notification
     */
    public Notification markAsRead(Notification notification) {
        if (notification != null) notification.setSeen(true);
        else {
            throw new IllegalArgumentException(ERROR_PREFIX + "Notification cannot be null!");
        }
        return notificationRepository.save(notification);
    }

    /**
     * Updates an existing Notification
     *
     * @param notification
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return the updated Notification
     */
    @Transactional
    public Notification updateNotification(
            Notification notification, String title, String body, Student student, Admin sender) {
        StringBuilder error = new StringBuilder();
        if (notification == null) {
            error.append("Notification to update cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("Notification title cannot be empty! ");
        }
        if (body != null && body.trim().length() == 0) {
            error.append("Notification body cannot be empty! ");
        }
        if (student == null) {
            error.append("Notification must have a Student receiver! ");
        }
        if (sender == null) {
            error.append("Notification must have an Admin sender!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (title != null) {
            notification.setTitle(title.trim());
        }
        if (body != null) {
            notification.setBody(body.trim());
        }
        if (sender != null) {
            notification.setSender(sender);
        }
        if (student != null) {
            notification.setStudent(student);
        }

        return notificationRepository.save(notification);
    }

    /**
     * Deletes an existing Notification
     *
     * @param notification
     * @return the deleted Notification
     */
    @Transactional
    public Notification deleteNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Notification to delete cannot be null!");
        }

        Student s = notification.getStudent();
        // make a new Set to avoid pointer issues
        Set<Notification> notifs = new HashSet<>(s.getNotifications());
        notifs.remove(notification);
        s.setNotifications(notifs);
        studentRepository.save(s);

        Admin a = notification.getSender();
        Set<Notification> adminNotifs = a.getSentNotifications();
        adminNotifs.remove(notification);
        a.setSentNotifications(adminNotifs);
        adminRepository.save(a);

        notificationRepository.delete(notification);

        return notification;
    }
}
