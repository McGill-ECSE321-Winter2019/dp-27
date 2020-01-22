package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CooperatorServiceStudentTests {

    @Autowired StudentService studentService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CoopService coopService;
    @Autowired NotificationService notificationService;
    @Autowired AdminService adminService;

    @Autowired StudentRepository studentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired NotificationRepository notificationRepository;
    @Autowired AdminRepository adminRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    public void testCreateStudent() {
        String firstName = "Albert";
        String lastName = "Kragl";
        String email = "frisbeeGod47@gmail.com";
        String studentID = "260735111";

        try {
            studentService.createStudent(firstName, lastName, email, studentID);
        } catch (IllegalArgumentException e) {
            fail();
        }

        Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentID);
    }

    @Test
    public void testCreateStudentWithNotifications() {
        String firstName = "Albert";
        String lastName = "Kragl";
        String email = "frisbeeGod47@gmail.com";
        String studentID = "260735111";
        Set<Coop> coops = new HashSet<>();
        Set<Notification> notifications = new HashSet<>();

        try {
            studentService.createStudent(
                    firstName, lastName, email, studentID, coops, notifications);
        } catch (IllegalArgumentException e) {
            fail();
        }

        Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentID);
    }

    @Test
    public void testCreateStudentNull() {
        try {
            studentService.createStudent(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Student first name cannot be empty. Student last name cannot be empty. Student email cannot be empty. Student ID cannot be empty.",
                    e.getMessage().trim());
        }
    }

    @Test
    public void testCreateStudentEmpty() {
        try {
            studentService.createStudent("", "", "", "");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Student first name cannot be empty. Student last name cannot be empty. Student email cannot be empty. Student ID cannot be empty.",
                    e.getMessage().trim());
        }
    }

    @Test
    public void testCreateStudentSpaces() {
        try {
            studentService.createStudent(" ", " ", " ", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Student first name cannot be empty. Student last name cannot be empty. Student email cannot be empty. Student ID cannot be empty.",
                    e.getMessage().trim());
        }
    }

    @Test
    public void testUpdateStudent() {
        String firstName = "Kah";
        String lastName = "Shew";
        String email = "kah.shew@nuts.com";
        String studentID = "260727105";

        try {
            studentService.createStudent(firstName, lastName, email, studentID);
        } catch (IllegalArgumentException e) {
            fail();
        }
        firstName = "P";
        lastName = "Nut";
        email = "p.nut@legume.com";
        String studentIDNew = "260745100";
        Set<Coop> coops = new HashSet<>();
        Set<Notification> notifications = new HashSet<>();

        try {
            Student s = studentService.getStudentByStudentID(studentID);
            studentService.updateStudent(
                    s, firstName, lastName, email, studentIDNew, coops, notifications);
        } catch (IllegalArgumentException e) {
            fail();
        }
        Student s = studentService.getStudentByStudentID(studentIDNew);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentIDNew);
    }

    @Test
    public void testUpdateStudentInvalid() {
        String firstName = "Kah";
        String lastName = "Shew";
        String email = "kah.shew@nuts.com";
        String studentID = "260727105";

        try {
            studentService.createStudent(firstName, lastName, email, studentID);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            Student s = studentService.getStudentByStudentID(studentID);
            studentService.updateStudent(s, "  ", "", "", "", null, null);
            fail();
        } catch (IllegalArgumentException e) {

        }
        Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentID);
    }

    @Test
    public void testDeleteStudent() {
        String firstName = "Kah";
        String lastName = "Shew";
        String email = "kah.shew@nuts.com";
        String studentID = "260727105";

        try {
            studentService.createStudent(firstName, lastName, email, studentID);
        } catch (IllegalArgumentException e) {
            fail();
        }

        Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentID);

        try {
            studentService.deleteStudentByStudentID(studentID);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
}
