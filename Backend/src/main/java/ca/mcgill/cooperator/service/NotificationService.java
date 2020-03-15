package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    @Autowired NotificationRepository notificationRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired AdminRepository adminRepository;

    /**
     * creates a Notification with title, body, admin sender and student receiver
     *
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return created notification
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
            throw new IllegalArgumentException(error.toString().trim());
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
     * retrieves a notification with specific id from the database
     *
     * @param id
     * @return notification with given id
     */
    @Transactional
    public Notification getNotification(int id) {
        Notification n = notificationRepository.findById(id).orElse(null);
        if (n == null) {
            throw new IllegalArgumentException("Notification with ID " + id + " does not exist!");
        }

        return n;
    }

    /**
     * returns all notifications in the database
     *
     * @return all notifications
     */
    @Transactional
    public List<Notification> getAllNotifications() {
        return ServiceUtils.toList(notificationRepository.findAll());
    }
    
    /**
     * returns all notifications for student in the database
     *
     * @return all notifications
     */
    @Transactional
    public List<Notification> getAllNotificationsOfStudent(Student student) {
        return ServiceUtils.toList(notificationRepository.findByStudent(student));
    }
    
    /**
     * returns all unseen notifications for student id
     *
     * @return all unseen notifications
     */
    @Transactional
    public List<Notification> getUnreadForStudent(Student student) {
    	List<Notification> unread = new ArrayList<>();
    	for(Notification n : notificationRepository.findByStudent(student)) {
    		if(!n.getSeen())
    			unread.add(n);
    	}
    	return unread;
    }
    
    
    /**
     * Set all Notifications of Student to seen
     *
     * @return all Notifications 
     */
    public List<Notification> markAllAsRead(Student s) {
    	for(Notification n : notificationRepository.findByStudent(s)) {
    		markAsRead(n);
    	}
    	return notificationRepository.findByStudent(s);
    }

    /**
     * set notification to seen
     *
     * @return notification seen
     */
    public Notification markAsRead(Notification n) {
        if (n != null) n.setSeen(true);
        else {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        notificationRepository.save(n);
        return n;
    }

    /**
     * updates an already existing notification
     *
     * @param n
     * @param title
     * @param body
     * @param student
     * @param sender
     * @return updated notification
     */
    @Transactional
    public Notification updateNotification(
            Notification n, String title, String body, Student student, Admin sender) {
        StringBuilder error = new StringBuilder();
        if (n == null) {
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
            throw new IllegalArgumentException(error.toString().trim());
        }

        if (title != null) {
            n.setTitle(title.trim());
        }
        if (body != null) {
            n.setBody(body.trim());
        }
        if (sender != null) {
            n.setSender(sender);
        }
        if (student != null) {
            n.setStudent(student);
        }

        return notificationRepository.save(n);
    }

    /**
     * deletes a notification from the database
     *
     * @param n
     * @return deleted notification
     */
    @Transactional
    public Notification deleteNotification(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException("Notification to delete cannot be null!");
        }

        Student s = n.getStudent();
        Set<Notification> notifs = s.getNotifications();
        notifs.remove(n);
        s.setNotifications(notifs);
        studentRepository.save(s);

        Admin a = n.getSender();
        List<Notification> adminNotifs = a.getSentNotifications();
        adminNotifs.remove(n);
        a.setSentNotifications(adminNotifs);
        adminRepository.save(a);

        notificationRepository.delete(n);

        return n;
    }
}
