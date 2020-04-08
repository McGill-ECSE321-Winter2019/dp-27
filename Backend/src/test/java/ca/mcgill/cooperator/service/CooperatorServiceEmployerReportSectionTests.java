package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.Student;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceEmployerReportSectionTests extends BaseServiceTest {

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired ReportConfigRepository reportConfigRepository;

    @Autowired EmployerContactService employerContactService;
    @Autowired AuthorRepository authorRepository;
    @Autowired ReportSectionService reportSectionService;
    @Autowired ReportService reportService;
    @Autowired CoopService coopService;
    @Autowired CompanyService companyService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<ReportSection> reportSections =
                reportSectionService.getAllReportSections();
        for (ReportSection reportSection : reportSections) {
            reportSectionService.deleteReportSection(reportSection);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        authorRepository.deleteAll();
        employerContactRepository.deleteAll();
        studentRepository.deleteAll();
        reportSectionRepository.deleteAll();
        reportRepository.deleteAll();
        companyRepository.deleteAll();
        reportConfigRepository.deleteAll();
    }

    @Test
    public void createReportSection() {
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            reportSectionService.createReportSection(response, rsConfig, r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionNull() {
        String error = "";
        try {
            reportSectionService.createReportSection(null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Report cannot be null!",
                error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionSpaces() {
        String error = "";
        try {
            reportSectionService.createReportSection("    ", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Report cannot be null!",
                error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionEmpty() {
        String error = "";
        try {
            reportSectionService.createReportSection("", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Report cannot be null!",
                error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSection() {
        ReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            rs = reportSectionService.createReportSection(response, rsConfig, r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // update response
        response = "This is my new response";
        try {
            rs = reportSectionService.updateReportSection(rs, response, null, null);
        } catch (IllegalArgumentException e) {
            e.getMessage();
            fail();
        }

        assertEquals(r.getId(), rs.getReport().getId());
        assertEquals(1, reportSectionService.getAllReportSections().size());
        r = reportService.getReport(r.getId());
        assertEquals(
                response,
                ((ReportSection) r.getReportSections().toArray()[0]).getResponse());
    }

    @Test
    public void updateReportSectionInvalid() {
        ReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            reportSectionService.createReportSection(response, rsConfig, r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            rs = reportSectionService.updateReportSection(rs, "", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Report section cannot be null! "
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
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            reportSectionService.createReportSection(response, rsConfig, r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            reportSectionService.updateReportSection(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report section cannot be null!", error);
    }

    @Test
    public void deleteReportSection() {
        ReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            rs = reportSectionService.createReportSection(response, rsConfig, r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            reportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void deleteReportSectionEmployerReport() {
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);
        Report r = createTestEmployerReport(reportService, coop, ec);

        try {
            reportSectionService.createReportSection(response, rsConfig, r);

            reportService.deleteReport(r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportSectionService.getAllReportSections().size());
        assertEquals(0, reportService.getAllReports().size());
    }

    @Test
    public void testDeleteReportSectionInvalid() {
        String error = "";
        try {
            reportSectionService.deleteReportSection(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report section to delete cannot be null!", error);
    }
}
