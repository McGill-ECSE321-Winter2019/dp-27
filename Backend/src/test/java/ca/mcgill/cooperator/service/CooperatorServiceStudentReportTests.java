package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceStudentReportTests extends BaseServiceTest {
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired StudentReportSectionRepository studentReportSectionRepository;
    @Autowired ReportConfigRepository reportConfigRepository;

    @Autowired StudentReportService studentReportService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<StudentReport> studentReports = studentReportService.getAllStudentReports();
        for (StudentReport studentReport : studentReports) {
            studentReport.setReportSections(new HashSet<StudentReportSection>());
            studentReportRepository.save(studentReport);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        studentReportSectionRepository.deleteAll();
        studentReportRepository.deleteAll();
        reportConfigRepository.deleteAll();
    }

    @Test
    public void testCreateStudentReport() {
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            studentReportService.createStudentReport(
                    ReportStatus.COMPLETED, coop, "Offer Letter", multipartFile);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, studentReportService.getAllStudentReports().size());
        coop = coopService.getCoopById(coop.getId());
        assertEquals(
                "Offer Letter", ((StudentReport) coop.getStudentReports().toArray()[0]).getTitle());
    }

    @Test
    public void testCreateStudentReportNull() {
        String error = "";
        try {
            studentReportService.createStudentReport(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Report Status cannot be null! "
                        + "Coop cannot be null! "
                        + "File title cannot be empty!",
                error);
        assertEquals(0, studentReportService.getAllStudentReports().size());
    }

    @Test
    public void testUpdateStudentReportWithReportSections() {
        StudentReport sr = null;

        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);

        // 1. create Student Report
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            sr =
                    studentReportService.createStudentReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", multipartFile);
        } catch (Exception e) {
            fail();
        }

        Set<StudentReportSection> sections = new HashSet<StudentReportSection>();
        StudentReportSection rs =
                createTestStudentReportSection(studentReportSectionService, rsConfig, sr);
        sections.add(rs);

        // 2. update with valid values
        try {
            sr =
                    studentReportService.updateStudentReport(
                            sr,
                            ReportStatus.COMPLETED,
                            "Offer Letter",
                            coop,
                            sections,
                            multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, sr.getReportSections().size());
        assertEquals(1, studentReportService.getAllStudentReports().size());
        coop = coopService.getCoopById(coop.getId());
        assertEquals(
                "Offer Letter", ((StudentReport) coop.getStudentReports().toArray()[0]).getTitle());
        rs = studentReportSectionService.getReportSection(rs.getId());
        assertEquals("Offer Letter", rs.getStudentReport().getTitle());
    }

    @Test
    public void testUpdateStudentReport() {
        StudentReport sr = null;

        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);

        // 1. create Student Report
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            sr =
                    studentReportService.createStudentReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", multipartFile);
        } catch (Exception e) {
            fail();
        }

        Set<StudentReportSection> sections = new HashSet<StudentReportSection>();
        StudentReportSection rs =
                createTestStudentReportSection(studentReportSectionService, rsConfig, sr);
        sections.add(rs);

        // 2. update with valid values
        try {
            sr =
                    studentReportService.updateStudentReport(
                            sr,
                            ReportStatus.INCOMPLETE,
                            "Offer Letter",
                            coop,
                            sections,
                            multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, sr.getReportSections().size());
        assertEquals(
                ReportStatus.INCOMPLETE,
                studentReportService.getStudentReport(sr.getId()).getStatus());
        assertEquals(1, studentReportService.getAllStudentReports().size());
    }

    @Test
    public void testUpdateStudentReportInvalid() {
        StudentReport sr = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        // 1. create Student Report
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            sr =
                    studentReportService.createStudentReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", multipartFile);
        } catch (Exception e) {
            fail();
        }

        // 2. try updating with invalid values
        String error = "";
        try {
            sr = studentReportService.updateStudentReport(null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Student Report cannot be null!", error);
        assertEquals(
                ReportStatus.COMPLETED,
                studentReportService.getStudentReport(sr.getId()).getStatus());
        assertEquals(1, studentReportService.getAllStudentReports().size());
    }

    @Test
    public void testDeleteStudentReport() {
        StudentReport sr = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        // 1. create Student Report
        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            sr =
                    studentReportService.createStudentReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", multipartFile);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, studentReportService.getAllStudentReports().size());

        // 2. try deleting the report
        try {
            studentReportService.deleteStudentReport(sr);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, studentReportService.getAllStudentReports().size());
    }

    @Test
    public void testDeleteStudentReportInvalid() {
        String error = "";
        try {
            studentReportService.deleteStudentReport(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Student Report to delete cannot be null!", error);
    }
}
