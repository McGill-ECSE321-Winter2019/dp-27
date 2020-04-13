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
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
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
public class CooperatorServiceEmployerReportTests extends BaseServiceTest {

    @Autowired ReportRepository reportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;

    @Autowired ReportService reportService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CompanyService companyService;
    @Autowired AuthorRepository authorRepository;
    @Autowired EmployerContactService employerContactService;
    @Autowired StudentService studentService;
    @Autowired ReportSectionService reportSectionService;
    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<Report> reports = reportService.getAllReports();
        for (Report r : reports) {
            r.setCoop(null);
            r.setReportSections(new HashSet<ReportSection>());
            r.setReportConfig(null);
            reportRepository.save(r);
        }

        List<ReportSection> reportSections = reportSectionService.getAllReportSections();
        for (ReportSection reportSection : reportSections) {
            reportSectionService.deleteReportSection(reportSection);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        authorRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        studentRepository.deleteAll();
        reportSectionRepository.deleteAll();
        reportSectionConfigRepository.deleteAll();
        reportRepository.deleteAll();
        reportConfigRepository.deleteAll();
    }

    @Test
    public void testCreateEmployerReport() {
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);

        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            reportService.createReport(
                    ReportStatus.COMPLETED, coop, "Offer Letter", ec, multipartFile, rc);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, reportService.getAllReports().size());
        ec = employerContactService.getEmployerContact(ec.getId());
        assertEquals("Offer Letter", ((Report) ec.getReports().toArray()[0]).getTitle());
        coop = coopService.getCoopById(coop.getId());
        assertEquals("Offer Letter", ((Report) coop.getReports().toArray()[0]).getTitle());
    }

    @Test
    public void testCreateEmployerReportNull() {
        String error = "";
        try {
            reportService.createReport(null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Report Status cannot be null! "
                        + "Coop cannot be null! "
                        + "Author cannot be null! "
                        + "File title cannot be empty! "
                        + "Report Config cannot be null!",
                error);
    }

    @Test
    public void testUpdateEmployerReportWithReportSections() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);

        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", ec, multipartFile, rc);
        } catch (Exception e) {
            fail();
        }

        Set<ReportSection> sections = new HashSet<ReportSection>();
        ReportSection rs = createTestReportSection(reportSectionService, rsConfig, r);
        sections.add(rs);

        try {
            r =
                    reportService.updateReport(
                            r,
                            ReportStatus.COMPLETED,
                            coop,
                            "Offer Letter",
                            ec,
                            rc,
                            sections,
                            multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, r.getReportSections().size());
        assertEquals(1, reportService.getAllReports().size());
    }

    @Test
    public void testUpdateEmployerReport() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService, rc);

        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", ec, multipartFile, rc);
        } catch (Exception e) {
            fail();
        }

        Set<ReportSection> sections = new HashSet<ReportSection>();
        ReportSection rs = createTestReportSection(reportSectionService, rsConfig, r);
        sections.add(rs);

        try {
            reportService.updateReport(
                    r, ReportStatus.INCOMPLETE, coop, "Offer Letter", ec, rc, sections, multipartFile);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, r.getReportSections().size());
        assertEquals(ReportStatus.INCOMPLETE, reportService.getReport(r.getId()).getStatus());
        assertEquals(1, reportService.getAllReports().size());
        ec = employerContactService.getEmployerContact(ec.getId());
        assertEquals("Offer Letter", ((Report) ec.getReports().toArray()[0]).getTitle());
        coop = coopService.getCoopById(coop.getId());
        assertEquals("Offer Letter", ((Report) coop.getReports().toArray()[0]).getTitle());
        rs = reportSectionService.getReportSection(rs.getId());
        assertEquals("Offer Letter", rs.getReport().getTitle());
    }

    @Test
    public void testUpdateEmployerReportInvalid() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);

        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", ec, multipartFile, rc);
        } catch (Exception e) {
            fail();
        }

        String error = "";
        try {
            r = reportService.updateReport(null, null, null, null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report cannot be null!", error);
        assertEquals(ReportStatus.COMPLETED, reportService.getReport(r.getId()).getStatus());
        assertEquals(1, reportService.getAllReports().size());
    }

    @Test
    public void testDeleteEmployerReport() {
        Report r = null;
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company company = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, company);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);

        try {
            MultipartFile multipartFile =
                    new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

            r =
                    reportService.createReport(
                            ReportStatus.COMPLETED, coop, "Offer Letter", ec, multipartFile, rc);
        } catch (Exception e) {
            fail();
        }

        assertEquals(1, reportService.getAllReports().size());

        try {
            reportService.deleteReport(r);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportService.getAllReports().size());
    }

    @Test
    public void testDeleteEmployerReportInvalid() {
        String error = "";
        try {
            reportService.deleteReport(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Report to delete cannot be null!", error);
    }
}
