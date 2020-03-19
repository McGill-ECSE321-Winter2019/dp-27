package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.EmployerReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
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
    @Autowired EmployerReportSectionRepository employerReportSectionRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;

    @Autowired EmployerContactService employerContactService;
    @Autowired EmployerReportSectionService employerReportSectionService;
    @Autowired EmployerReportService employerReportService;
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
        List<EmployerReportSection> reportSections =
                employerReportSectionService.getAllReportSections();
        for (EmployerReportSection reportSection : reportSections) {
            employerReportSectionService.deleteReportSection(reportSection);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        employerContactRepository.deleteAll();
        studentRepository.deleteAll();
        employerReportSectionRepository.deleteAll();
        employerReportRepository.deleteAll();
        companyRepository.deleteAll();
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
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            employerReportSectionService.createReportSection(response, rsConfig, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, employerReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionNull() {
        String error = "";
        try {
            employerReportSectionService.createReportSection(null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Employer report cannot be null!",
                error);
        assertEquals(0, employerReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionSpaces() {
        String error = "";
        try {
            employerReportSectionService.createReportSection("    ", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Employer report cannot be null!",
                error);
        assertEquals(0, employerReportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionEmpty() {
        String error = "";
        try {
            employerReportSectionService.createReportSection("", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Response cannot be empty! "
                        + "Report section config cannot be null! "
                        + "Employer report cannot be null!",
                error);
        assertEquals(0, employerReportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSection() {
        EmployerReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            rs = employerReportSectionService.createReportSection(response, rsConfig, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // update response
        response = "This is my new response";
        try {
            rs = employerReportSectionService.updateReportSection(rs, response, null, null);
        } catch (IllegalArgumentException e) {
            e.getMessage();
            fail();
        }

        assertEquals(er.getId(), rs.getEmployerReport().getId());
        assertEquals(1, employerReportSectionService.getAllReportSections().size());
        er = employerReportService.getEmployerReport(er.getId());
        assertEquals(
                response,
                ((EmployerReportSection) er.getReportSections().toArray()[0]).getResponse());
    }

    @Test
    public void updateReportSectionInvalid() {
        EmployerReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            employerReportSectionService.createReportSection(response, rsConfig, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            rs = employerReportSectionService.updateReportSection(rs, "", null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(
                ERROR_PREFIX
                        + "Employer report section cannot be null! "
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
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            employerReportSectionService.createReportSection(response, rsConfig, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        String error = "";
        try {
            employerReportSectionService.updateReportSection(null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Employer report section cannot be null!", error);
    }

    @Test
    public void deleteReportSection() {
        EmployerReportSection rs = null;
        String response = "This is a response";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student s = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, s);
        Company c = createTestCompany(companyService);
        EmployerContact ec = createTestEmployerContact(employerContactService, c);
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            rs = employerReportSectionService.createReportSection(response, rsConfig, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        try {
            employerReportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, employerReportSectionService.getAllReportSections().size());
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
        ReportSectionConfig rsConfig =
                createTestReportSectionConfig(reportConfigService, reportSectionConfigService);
        EmployerReport er = createTestEmployerReport(employerReportService, coop, ec);

        try {
            employerReportSectionService.createReportSection(response, rsConfig, er);

            employerReportService.deleteEmployerReport(er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, employerReportSectionService.getAllReportSections().size());
        assertEquals(0, employerReportService.getAllEmployerReports().size());
    }

    @Test
    public void testDeleteReportSectionInvalid() {
        String error = "";
        try {
            employerReportSectionService.deleteReportSection(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals(ERROR_PREFIX + "Employer report section to delete cannot be null!", error);
    }
}
