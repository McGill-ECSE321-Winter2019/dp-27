package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceStudentReportSectionTests extends BaseServiceTest {

    @Autowired StudentReportSectionRepository studentReportSectionRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired StudentRepository studentRepository;

    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired StudentReportService studentReportService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<StudentReportSection> reportSections =
                studentReportSectionService.getAllReportSections();
        for (StudentReportSection reportSection : reportSections) {
            studentReportSectionService.deleteReportSection(reportSection);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        employerContactRepository.deleteAll();
        studentRepository.deleteAll();
        studentReportSectionRepository.deleteAll();
        studentReportRepository.deleteAll();
    }

    @Test
    public void createReportSection() {
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            studentReportSectionService.createReportSection(response, rsConfig, sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionNull() {
        String error = "";
        try {
            studentReportSectionService.createReportSection(null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Student report cannot be null!",
                error);
        assertEquals(0, studentReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionSpaces() {
        String error = "";
        try {
            studentReportSectionService.createReportSection("    ", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Student report cannot be null!",
                error);
        assertEquals(0, studentReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionEmpty() {
        String error = "";
        try {
            studentReportSectionService.createReportSection("", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Student report cannot be null!",
                error);
        assertEquals(0, studentReportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSection() {
        StudentReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            rs = studentReportSectionService.createReportSection(response, rsConfig, sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentReportSectionService.getAllReportSections().size());

        // update response
        response = "This is my new response";
        try {
            rs = studentReportSectionService.updateReportSection(rs, response, null, null);
        } catch (IllegalArgumentException e) {
            e.getMessage();
            fail();
        }

        assertEquals(sr.getId(), rs.getStudentReport().getId());
        assertEquals(1, studentReportSectionService.getAllReportSections().size());
        sr = studentReportService.getStudentReport(sr.getId());
        assertEquals(
                response,
                ((StudentReportSection) sr.getReportSections().toArray()[0]).getResponse());
    }

    @Test
    public void updateReportSectionInvalid() {
        StudentReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            studentReportSectionService.createReportSection(response, rsConfig, sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentReportSectionService.getAllReportSections().size());

        String error = "";
        try {
            rs = studentReportSectionService.updateReportSection(rs, "", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Student report section cannot be null! "
                        + "Response cannot be empty!",
                error);
    }

    @Test
    public void updateReportSectionNull() {
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            studentReportSectionService.createReportSection(response, rsConfig, sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentReportSectionService.getAllReportSections().size());

        String error = "";
        try {
            studentReportSectionService.updateReportSection(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Student report section cannot be null!", error);
    }

    @Test
    public void deleteReportSection() {
        StudentReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            rs = studentReportSectionService.createReportSection(response, rsConfig, sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, studentReportSectionService.getAllReportSections().size());

        try {
            studentReportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, studentReportSectionService.getAllReportSections().size());
    }

    @Test
    public void deleteReportSectionStudentReport() {
        StudentReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        StudentReport sr = createTestStudentReport(studentReportService, coop);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);

        try {
            rs = studentReportSectionService.createReportSection(response, rsConfig, sr);

            studentReportService.deleteStudentReport(rs.getStudentReport());
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, studentReportSectionService.getAllReportSections().size());
        assertEquals(0, studentReportService.getAllStudentReports().size());
    }

    @Test
    public void testDeleteReportSectionInvalid() {
        String error = "";
        try {
            studentReportSectionService.deleteReportSection(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Student report section to delete cannot be null!", error);
    }
}
