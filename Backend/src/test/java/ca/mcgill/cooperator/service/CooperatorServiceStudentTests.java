package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
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
        adminRepository.deleteAll();
    }

    @Test
    public void testCreateStudent1() {
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
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    public void testCreateStudentWithNotifications() {
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
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    public void testCreateStudentNull() {
        String error = "";
        try {
            studentService.createStudent(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Student first name cannot be empty. "
                        + "Student last name cannot be empty. "
                        + "Student email cannot be empty. "
                        + "Student ID cannot be null or invalid.",
                error);
    }

    @Test
    public void testCreateStudentEmpty() {
        String error = "";
        try {
            studentService.createStudent("", "", "", "");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Student first name cannot be empty. "
                        + "Student last name cannot be empty. "
                        + "Student email cannot be empty. "
                        + "Student ID cannot be null or invalid.",
                error);
    }

    @Test
    public void testCreateStudentSpaces() {
        String error = "";
        try {
            studentService.createStudent("   ", "     ", " ", "      ");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                "Student first name cannot be empty. "
                        + "Student last name cannot be empty. "
                        + "Student email cannot be empty. "
                        + "Student ID cannot be null or invalid.",
                error);
    }

    @Test
    public void testUpdateStudent1() {
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
    public void testUpdateStudent2() {
        String firstName = "Susan";
        String lastName = "Matuszewski";
        String email = "susan@gmail.com";
        String studentId = "123445660";
        Student s = new Student();

        try {
            s = studentService.createStudent(firstName, lastName, email, studentId);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentService.getAllStudents().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Coop coop = createTestCoop(courseOffering, s);
        Set<Coop> coops = new HashSet<Coop>();
        coops.add(coop);
        Admin a = createTestAdmin();
        Notification notif = createTestNotification(s, a);
        Set<Notification> notifs = new HashSet<Notification>();
        notifs.add(notif);

        try {
            s =
                    studentService.updateStudent(
                            s, firstName, lastName, email, studentId, coops, notifs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, s.getCoops().size());
        assertEquals(1, studentService.getAllStudents().size());
        coop = coopService.getCoopById(coop.getId());
        assertEquals(firstName, ((Coop)coop.getStudent().getCoops().toArray()[0]).getStudent().getFirstName());
        notif = notificationService.getNotification(notif.getId());
        assertEquals(firstName, notif.getStudent().getFirstName());
    }

    @Test
    public void testUpdateStudentInvalid1() {
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
    public void testUpdateStudentInvalid2() {
        String firstName = "Susan";
        String lastName = "Matuszewski";
        String email = "susan@gmail.com";
        String studentId = "123445660";

        try {
            studentService.createStudent(firstName, lastName, email, studentId);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentService.getAllStudents().size());

        String error = "";
        try {
            studentService.updateStudent(null, null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Student to update cannot be null.", error);
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent1() {
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
        assertEquals(0, studentService.getAllStudents().size());
    }

    @Test
    public void testDeleteStudent2() {
        String firstName = "Susan";
        String lastName = "Matuszewski";
        String email = "susan@gmail.com";
        String studentId = "123445660";
        Student s = new Student();
        try {
            s = studentService.createStudent(firstName, lastName, email, studentId);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentService.getAllStudents().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Coop coop = createTestCoop(courseOffering, s);
        Set<Coop> coops = new HashSet<Coop>();
        coops.add(coop);
        Admin a = createTestAdmin();
        Notification notif = createTestNotification(s, a);
        Set<Notification> notifs = new HashSet<Notification>();
        notifs.add(notif);

        try {
            s =
                    studentService.updateStudent(
                            s, firstName, lastName, email, studentId, coops, notifs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            studentService.deleteStudent(s);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, studentService.getAllStudents().size());
    }

    private Course createTestCourse() {
        Course c = null;
        c = courseService.createCourse("FACC200");
        return c;
    }

    private CourseOffering createTestCourseOffering(Course c) {
        CourseOffering co = null;
        co = courseOfferingService.createCourseOffering(2020, Season.WINTER, c);
        return co;
    }

    private Coop createTestCoop(CourseOffering co, Student s) {
        Coop coop = new Coop();
        coop = coopService.createCoop(CoopStatus.FUTURE, co, s);
        return coop;
    }

    private Admin createTestAdmin() {
        Admin a = new Admin();
        a = adminService.createAdmin("Emma", "Eagles", "emma@gmail.com");
        return a;
    }

    private Notification createTestNotification(Student s, Admin a) {
        Notification notif = new Notification();
        notif = notificationService.createNotification("title", "body", s, a);
        return notif;
    }
}
