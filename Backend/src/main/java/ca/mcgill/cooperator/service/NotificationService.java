package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
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
        notificationRepository.save(n);

        Set<Notification> notifs = student.getNotifications();
        notifs.add(n);
        student.setNotifications(notifs);
        
        List<Notification> senderNotifs = sender.getSentNotifications();
        senderNotifs.add(n);
        sender.setSentNotifications(senderNotifs);

        adminRepository.save(sender);
        studentRepository.save(student);

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
     * retrieves first notification in database with given title
     *
     * @param title
     * @return notification with given title
     */
    @Transactional
    public Notification getNotification(String title) {
        Notification n = notificationRepository.findByTitle(title.trim());
        if (n == null) {
            throw new IllegalArgumentException(
                    "Notification with title \"" + title + "\" does not exist!");
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

        // need to either replace or add notification in sender and student
        boolean sender_contains = false;
        boolean student_contains = false;

        List<Notification> sender_notifs = sender.getSentNotifications();
        Set<Notification> student_notifs = student.getNotifications();

        for (Notification sender_n : sender_notifs) {
            if (sender_n.getId() == n.getId()) {
                sender_contains = true;
                int sender_index = sender_notifs.indexOf(sender_n);
                sender_notifs.set(sender_index, n);
            }
        }

        for (Notification student_n : sender_notifs) {
            if (student_n.getId() == n.getId()) {
                sender_notifs.remove(student_n);
                sender_notifs.add(n);
                student_contains = true;
            }
        }

        n.setTitle(title.trim());
        n.setBody(body.trim());
        n.setSender(sender);
        n.setStudent(student);

        if (!sender_contains) {
            sender_notifs.add(n);
        }
        sender.setSentNotifications(sender_notifs);

        if (!student_contains) {
            student_notifs.add(n);
        }
        student.setNotifications(student_notifs);

        studentRepository.save(student);
        adminRepository.save(sender);

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
