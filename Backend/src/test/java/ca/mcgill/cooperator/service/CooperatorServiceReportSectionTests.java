package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class CooperatorServiceReportSectionTests {
    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired StudentRepository studentRepository;

    @Autowired ReportSectionService reportSectionService;
    @Autowired StudentReportService studentReportService;
    @Autowired EmployerReportService employerReportService;
    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CompanyService companyService;
    @Autowired EmployerContactService employerContactService;
    @Autowired StudentService studentService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<EmployerReport> ers = employerReportService.getAllEmployerReports();
        for (EmployerReport er : ers) {
            er.setCoop(null);
            employerReportRepository.save(er);
        }
        
        List<ReportSection> reportSections = reportSectionService.getAllReportSections();
        for (ReportSection reportSection : reportSections) {
            reportSectionService.deleteReportSection(reportSection);
        }

        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        studentRepository.deleteAll();
        reportSectionRepository.deleteAll();
        employerReportRepository.deleteAll();
        studentReportRepository.deleteAll();
    }

    @Test
    public void createReportSection() {
        String title = "Hello";
        String content = "This is a report section";

        try {
            reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionNull() {
        String title = null;
        String content = null;

        String error = "";
        try {
            reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Title cannot be empty! " + "Content cannot be empty!", error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionSpaces() {
        String title = "    ";
        String content = "            ";

        String error = "";
        try {
            reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Title cannot be empty! " + "Content cannot be empty!", error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void createReportSectionEmpty() {
        String title = "";
        String content = "";

        String error = "";
        try {
            reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Title cannot be empty! " + "Content cannot be empty!", error);
        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSectionStudentReport() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);
        StudentReport sr = createTestStudentReport(coop);

        try {
            rs = reportSectionService.updateReportSection(rs, title, content, sr, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(sr.getId(), rs.getStudentReport().getId());
        assertEquals(1, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSectionEmployerReport() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        EmployerReport er = createTestEmployerReport(coop, ec);

        try {
            rs = reportSectionService.updateReportSection(rs, title, content, null, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(er.getId(), rs.getEmployerReport().getId());
        assertEquals(1, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void updateReportSectionInvalid() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        StudentReport sr = new StudentReport();
        EmployerReport er = new EmployerReport();

        String error = "";
        try {
            rs = reportSectionService.updateReportSection(rs, title, content, sr, er);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Cannot add to both Student Report and Employer Report!", error);
    }

    @Test
    public void updateReportSectionNull() {
        String title = "Hello";
        String content = "This is a report section";

        try {
            reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        String error = "";
        try {
            reportSectionService.updateReportSection(null, null, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Report Section cannot be null!", error);
    }

    @Test
    public void deleteReportSection() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        try {
            reportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void deleteReportSectionStudentReport() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);
        StudentReport sr = createTestStudentReport(coop);

        try {
            rs = reportSectionService.updateReportSection(rs, title, content, sr, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(sr.getId(), rs.getStudentReport().getId());
        assertEquals(1, reportSectionService.getAllReportSections().size());

        try {
            reportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void deleteReportSectionEmployerReport() {
        ReportSection rs = null;
        String title = "Hello";
        String content = "This is a report section";

        try {
            rs = reportSectionService.createReportSection(title, content);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(1, reportSectionService.getAllReportSections().size());

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Student s = createTestStudent();
        Coop coop = createTestCoop(courseOffering, s);
        Company company = createTestCompany();
        EmployerContact ec = createTestEmployerContact(company);
        EmployerReport er = createTestEmployerReport(coop, ec);

        try {
            rs = reportSectionService.updateReportSection(rs, title, content, null, er);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(er.getId(), rs.getEmployerReport().getId());
        assertEquals(1, reportSectionService.getAllReportSections().size());

        try {
            reportSectionService.deleteReportSection(rs);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportSectionService.getAllReportSections().size());
    }

    @Test
    public void testDeleteReportSectionInvalid() {
        String error = "";
        try {
            reportSectionService.deleteReportSection(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Report Section to delete cannot be null!", error);
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

    private Company createTestCompany() {
        Company c = new Company();
        c =
                companyService.createCompany(
                        "Facebook",
                        "Menlo Park",
                        "California",
                        "USA",
                        new ArrayList<EmployerContact>());
        return c;
    }

    private Student createTestStudent() {
        Student s = new Student();
        s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");
        return s;
    }

    private EmployerContact createTestEmployerContact(Company c) {
        EmployerContact ec = new EmployerContact();
        ec =
                employerContactService.createEmployerContact(
                        "Emma", "Eags", "eags@gmail.com", "2143546578", c);
        return ec;
    }

    private StudentReport createTestStudentReport(Coop c) {
        StudentReport sr = new StudentReport();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));

            sr =
                    studentReportService.createStudentReport(
                            ReportStatus.COMPLETED, c, "Offer Letter", multipartFile);
            return sr;
        } catch (IOException e) {
            return null;
        }
    }

    private EmployerReport createTestEmployerReport(Coop c, EmployerContact ec) {
        EmployerReport er = new EmployerReport();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));
            er =
                    employerReportService.createEmployerReport(
                            ReportStatus.COMPLETED, c, "Offer Letter", ec, multipartFile);
        } catch (IOException e) {
            return null;
        }
        return er;
    }
}
