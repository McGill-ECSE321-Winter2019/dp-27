package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceAdminTests extends BaseServiceTest {

    @Autowired AdminService adminService;
    @Autowired NotificationService notificationService;
    @Autowired StudentService studentService;

    @Autowired AdminRepository adminRepository;
    @Autowired NotificationRepository notificationRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired AuthorRepository authorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        authorRepository.deleteAll();
        adminRepository.deleteAll();
        notificationRepository.deleteAll();
        studentRepository.deleteAll();
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
    public void testAdminUniqueEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "phooley@gmail.com";
        try {
            adminService.createAdmin(firstName, lastName, email);
            // email must be unique so expect a SQLException
            adminService.createAdmin(firstName, lastName, email);
        } catch (Exception e) {
            assertEquals(1, adminService.getAllAdmins().size());
        }
    }

    @Test
    public void testCreateAdminInvalidEmail() {
        String firstName = "Paul";
        String lastName = "Hooley";
        String email = "abcdefg";

        try {
            adminService.createAdmin(firstName, lastName, email);
        } catch (IllegalArgumentException e) {
            assertEquals(ERROR_PREFIX + "Admin email must be a valid email!", e.getMessage());
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
                ERROR_PREFIX
                        + "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty!",
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
                ERROR_PREFIX
                        + "Admin first name cannot be empty! Admin last name cannot be empty! Admin email cannot be empty!",
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
                ERROR_PREFIX
                        + "Admin first name cannot be empty! "
                        + "Admin last name cannot be empty! "
                        + "Admin email cannot be empty!",
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
            adminService.updateAdmin(a, firstName, lastName, email, a.getSentNotifications(), null);
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
        Notification n = new Notification();

        Admin a = null;
        try {
            adminService.createAdmin(firstName, lastName, email);
            a = adminService.getAdmin(email);

            n = createTestNotification(studentService, notificationService, a);
            Set<Notification> notifications = a.getSentNotifications();
            notifications.add(n);

            adminService.updateAdmin(a, firstName, lastName, email, notifications, null);
            a = adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        n = notificationService.getNotification(n.getId());

        assertEquals("Paul", n.getSender().getFirstName());
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
            adminService.getAdmin(email);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            adminService.updateAdmin(a, "", "   ", "", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Admin to update cannot be null! "
                        + "Admin first name cannot be empty! "
                        + "Admin last name cannot be empty! "
                        + "Admin email cannot be empty!",
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

        assertEquals(ERROR_PREFIX + "Admin to delete cannot be null!", error);
    }
}
