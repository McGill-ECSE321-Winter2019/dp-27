package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceNotificationTests extends BaseServiceTest {

    @Autowired NotificationService notificationService;
    @Autowired AdminService adminService;
    @Autowired StudentService studentService;

    @Autowired NotificationRepository notificationRepository;
    @Autowired AdminRepository adminRepository;
    @Autowired StudentRepository studentRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        notificationRepository.deleteAll();
        studentRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    public void testCreateNotification() {
        String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent(studentService);
        Admin sender = createTestAdmin(adminService);

        try {
            notificationService.createNotification(title, body, student, sender);

        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, notificationService.getAllNotifications().size());
        student = studentService.getStudentById(student.getId());
        assertEquals("Hello", ((Notification) student.getNotifications().toArray()[0]).getTitle());
        sender = adminService.getAdmin(sender.getId());
        assertEquals("Hello", sender.getSentNotifications().iterator().next().getTitle());
    }

    @Test
    public void testCreateNotificationSetSeen() {
        String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent(studentService);
        Admin sender = createTestAdmin(adminService);

        try {
            notificationService.createNotification(title, body, student, sender);

        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, notificationService.getAllNotifications().size());
        assertFalse(notificationService.getAllNotifications().get(0).getSeen());

        try {
            notificationService.markAsRead(notificationService.getAllNotifications().get(0));

        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, notificationService.getAllNotifications().size());
        assertTrue(notificationService.getAllNotifications().get(0).getSeen());
    }

    @Test
    public void testCreateNotificationNull() {
        String title = null;
        String body = null;
        Student student = null;
        Admin sender = null;

        String error = "";
        try {
            notificationService.createNotification(title, body, student, sender);

        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Notification title cannot be empty! "
                        + "Notification body cannot be empty! "
                        + "Notification must have a Student receiver! "
                        + "Notification must have an Admin sender!",
                error);
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    public void testCreateNotificationEmpty() {
        String title = "";
        String body = "";
        Student student = null;
        Admin sender = null;

        String error = "";
        try {
            notificationService.createNotification(title, body, student, sender);

        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Notification title cannot be empty! "
                        + "Notification body cannot be empty! "
                        + "Notification must have a Student receiver! "
                        + "Notification must have an Admin sender!",
                error);
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    public void testCreateNotificationSpaces() {
        String title = "     ";
        String body = "  ";
        Student student = null;
        Admin sender = null;

        String error = "";
        try {
            notificationService.createNotification(title, body, student, sender);

        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Notification title cannot be empty! "
                        + "Notification body cannot be empty! "
                        + "Notification must have a Student receiver! "
                        + "Notification must have an Admin sender!",
                error);
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    public void testUpdateNotification() {
        String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent(studentService);
        Admin sender = createTestAdmin(adminService);

        Notification n = null;

        try {
            n = notificationService.createNotification(title, body, student, sender);

            title = "Bye";
            body = "different message";

            n = notificationService.updateNotification(n, title, body, student, sender);

        } catch (IllegalArgumentException e) {
            fail();
        }

        sender = adminService.getAdmin(sender.getId());

        student = studentService.getStudentById(student.getId());

        assertEquals("Bye", n.getTitle());
        assertEquals("Bye", sender.getSentNotifications().iterator().next().getTitle());
        assertEquals("Bye", ((Notification) student.getNotifications().toArray()[0]).getTitle());
        assertEquals(1, notificationService.getAllNotifications().size());
    }

    @Test
    public void testUpdateNotificationInvalid() {
        String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent(studentService);
        Admin sender = createTestAdmin(adminService);

        Notification n = null;

        try {
            n = notificationService.createNotification(title, body, student, sender);

            title = "    ";
            body = "  ";

            n = notificationService.updateNotification(n, title, body, null, null);

        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            assertEquals(
                    ERROR_PREFIX
                            + "Notification title cannot be empty! "
                            + "Notification body cannot be empty! "
                            + "Notification must have a Student receiver! "
                            + "Notification must have an Admin sender!",
                    error);
        }
    }

    @Test
    public void testDeleteNotification() {
        String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent(studentService);
        Admin sender = createTestAdmin(adminService);

        Notification n = null;

        try {
            n = notificationService.createNotification(title, body, student, sender);
            assertEquals(1, notificationService.getAllNotifications().size());

            notificationService.deleteNotification(n);

        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, notificationService.getAllNotifications().size());
    }
}
