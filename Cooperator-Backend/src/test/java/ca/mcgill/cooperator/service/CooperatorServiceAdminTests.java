package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CooperatorServiceAdminTests {

    @Autowired AdminService adminService;

    @Autowired AdminRepository adminRepository;

    @Autowired NotificationRepository notificationRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        adminRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    public void testCreateAdmin() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, adminService.getAllAdmins().size());
    }

    @Test
    public void testCreateAdminInvalidEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "abcdefg";

        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            assertEquals("Admin email must be a valid email!", e.getMessage());
        }
    }

    @Test
    public void testCreateAdminNull() {
        String firstName = null;
        String lastName = null;
        String email = null;

        String error = "";
        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty!",
                error);
        assertEquals(0, adminService.getAllAdmins().size());
    }

    @Test
    public void testCreateAdminEmpty() {
        String firstName = "";
        String lastName = "";
        String email = "";

        String error = "";
        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty!",
                error);
        assertEquals(0, adminService.getAllAdmins().size());
    }

    @Test
    public void testCreateAdminSpaces() {
        String firstName = " ";
        String lastName = "  ";
        String email = "     ";

        String error = "";
        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty!",
                error);
        assertEquals(0, adminService.getAllAdmins().size());
    }

    @Test
    public void testUpdateAdmin() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";

        Admin a = null;
        try {
            adminService.createAdmin(firstName, lastName, email);
            a = adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        firstName = "Emma";
        lastName = "Eagles";
        email = "jeagles@gmail.com";
        try {
            adminService.updateAdmin(a, firstName, lastName, email, a.getSentNotifications());
            a = adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(firstName, a.getFirstName());
        assertEquals(1, adminService.getAllAdmins().size());
    }

    @Test
    public void testAddNotificationToAdmin() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";

        Admin a = null;
        try {
            adminService.createAdmin(firstName, lastName, email);
            a = adminService.getAdmin(email);

            Notification n = createTestNotification();
            List<Notification> notifications = a.getSentNotifications();
            notifications.add(n);

            adminService.updateAdmin(a, firstName, lastName, email, notifications);
            a = adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, a.getSentNotifications().size());
        assertEquals(lastName, a.getLastName());
        assertEquals(1, adminService.getAllAdmins().size());
    }

    @Test
    public void testUpdateAdminInvalid() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";

        Admin a = null;

        try {
            adminService.createAdmin(firstName, lastName, email);
            a = adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            adminService.updateAdmin(a, "", "   ", "", null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty! Admin sent notifications cannot be null!",
                error);

        // Original Admin should still exist
        assertEquals(1, adminService.getAllAdmins().size());
        try {
            adminService.getAdmin(email);
        } catch (IllegalArgumentException _e) {
            fail();
        }
    }

    @Test
    public void testDeleteAdmin() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";

        Admin a = null;
        try {
            adminService.createAdmin(firstName, lastName, email);
            a = adminService.getAdmin(email);
            adminService.deleteAdmin(a);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, adminService.getAllAdmins().size());
    }

    @Test
    public void testDeleteAdminInvalid() {
        String error = "";
        try {
            adminService.deleteAdmin(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Admin to delete cannot be null!", error);
    }

    private static Notification createTestNotification() {
        Notification n = new Notification();
        Student s = new Student();
        n.setTitle("Report Due");
        n.setBody("Report Due by April 2020");
        n.setStudent(s);

        return n;
    }
}
