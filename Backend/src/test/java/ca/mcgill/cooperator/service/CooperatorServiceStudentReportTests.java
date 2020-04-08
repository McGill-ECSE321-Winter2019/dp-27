package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Student;
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
    @Autowired ReportRepository reportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired ReportConfigRepository reportConfigRepository;

    @Autowired ReportService reportService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired ReportSectionService reportSectionService;
    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            report.setReportSections(new HashSet<ReportSection>());
            reportRepository.save(report);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        reportSectionRepository.deleteAll();
        reportRepository.deleteAll();
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

            reportService.createReport(
                    ReportStatus.COMPLETED, coop, "Offer Letter", s, multipartFile);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, reportService.getAllReports().size());
        coop = coopService.getCoopById(coop.getId());
        assertEquals("Offer Letter", ((Report) coop.getReports().toArray()[0]).getTitle());
    }

    @Test
    public void testCreateStudentReportNull() {
        String error = "";
        try {
            reportService.createReport(null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Report Status cannot be null! "
                        + "Coop cannot be null! "
                        + "Author cannot be null! "
                        + "File title cannot be empty!",
                error);
        assertEquals(0, reportService.getAllReports().size());
    }

    @Test
    public void testUpdateStudentReportWithReportSections() {
        Report r = null;

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

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", s, multipartFile);
        } catch (Exception e) {
            fail();
        }

        Set<ReportSection> sections = new HashSet<ReportSection>();
        ReportSection rs = createTestReportSection(reportSectionService, rsConfig, r);
        sections.add(rs);

        // 2. update with valid values
        try {
            r =
                    reportService.updateReport(
                            r,
                            ReportStatus.COMPLETED,
                            coop,
                            "Offer Letter",
                            s,
                            sections,
                            multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, r.getReportSections().size());
        assertEquals(1, reportService.getAllReports().size());
        coop = coopService.getCoopById(coop.getId());
        assertEquals("Offer Letter", ((Report) coop.getReports().toArray()[0]).getTitle());
        rs = reportSectionService.getReportSection(rs.getId());
        assertEquals("Offer Letter", rs.getReport().getTitle());
    }

    @Test
    public void testUpdateStudentReport() {
        Report r = null;

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

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", s, multipartFile);
        } catch (Exception e) {
            fail();
        }

        Set<ReportSection> sections = new HashSet<ReportSection>();
        ReportSection rs = createTestReportSection(reportSectionService, rsConfig, r);
        sections.add(rs);

        // 2. update with valid values
        try {
            r =
                    reportService.updateReport(
                            r,
                            ReportStatus.INCOMPLETE,
                            coop,
                            "Offer Letter",
                            s,
                            sections,
                            multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, r.getReportSections().size());
        assertEquals(ReportStatus.INCOMPLETE, reportService.getReport(r.getId()).getStatus());
        assertEquals(1, reportService.getAllReports().size());
    }

    @Test
    public void testUpdateStudentReportInvalid() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        // 1. create Student Report
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", s, multipartFile);
        } catch (Exception e) {
            fail();
        }

        // 2. try updating with invalid values
        String error = "";
        try {
            r = reportService.updateReport(null, null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report cannot be null!", error);
        assertEquals(ReportStatus.COMPLETED, reportService.getReport(r.getId()).getStatus());
        assertEquals(1, reportService.getAllReports().size());
    }

    @Test
    public void testDeleteStudentReport() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);

        // 1. create Student Report
        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", s, multipartFile);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, reportService.getAllReports().size());

        // 2. try deleting the report
        try {
            reportService.deleteReport(r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportService.getAllReports().size());
    }

    @Test
    public void testDeleteStudentReportInvalid() {
        String error = "";
        try {
            reportService.deleteReport(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report to delete cannot be null!", error);
    }
}
