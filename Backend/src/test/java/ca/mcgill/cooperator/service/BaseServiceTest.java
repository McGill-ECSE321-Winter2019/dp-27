package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class BaseServiceTest {

    static final String ERROR_PREFIX = "ERROR [Service]: ";

    Admin createTestAdmin(AdminService service) {
        Admin a = new Admin();
        a = service.createAdmin("Lorraine", "Douglas", "lorraine@gmail.com");

        return a;
    }

    Course createTestCourse(CourseService service) {
        Course c = null;
        c = service.createCourse("FACC200");
        return c;
    }

    CourseOffering createTestCourseOffering(CourseOfferingService service, Course c) {
        CourseOffering co = null;
        co = service.createCourseOffering(2020, Season.WINTER, c);
        return co;
    }

    CourseOffering createTestCourseOffering(
            CourseOfferingService service, Course c, Season s, int y) {
        CourseOffering co = null;
        co = service.createCourseOffering(y, s, c);
        return co;
    }

    Coop createTestCoop(CoopService service, CourseOffering co, Student s) {
        Coop coop = new Coop();
        coop = service.createCoop(CoopStatus.FUTURE, co, s);
        return coop;
    }

    CoopDetails createTestCoopDetails(CoopDetailsService service, EmployerContact ec, Coop c) {
        CoopDetails cd = new CoopDetails();
        cd =
                service.createCoopDetails(
                        20, 40, Date.valueOf("2020-05-11"), Date.valueOf("2020-07-31"), ec, c);
        return cd;
    }

    Company createTestCompany(CompanyService service) {
        Company c = new Company();
        c =
                service.createCompany(
                        "Facebook",
                        "Menlo Park",
                        "California",
                        "USA",
                        new ArrayList<EmployerContact>());

        return c;
    }

    EmployerContact createTestEmployerContact(EmployerContactService service, Company c) {
        EmployerContact e = new EmployerContact();

        e = service.createEmployerContact("Albert", "Kragl", "albert@kragl.com", "123456678", c);

        return e;
    }

    Notification createTestNotification(
            StudentService studentService, NotificationService notifService, Admin a) {
        Student s =
                studentService.createStudent("Albert", "Kragl", "albert@kragl.com", "123456789");
        Notification n =
                notifService.createNotification("Report Due", "Report Due by April 2020", s, a);

        return n;
    }

    Notification createTestNotification(NotificationService service, Student s, Admin a) {
        Notification notif = new Notification();
        notif = service.createNotification("title", "body", s, a);
        return notif;
    }

    Student createTestStudent(StudentService service) {
        Student s = new Student();
        s = service.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
    }

    Report createTestStudentReport(ReportService service, Coop c, Student s, ReportConfig rc) {
        Report r = new Report();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));

            r = service.createReport(ReportStatus.COMPLETED, c, "Offer Letter", s, multipartFile, rc);
            return r;
        } catch (IOException e) {
            return null;
        }
    }

    ReportSection createTestReportSection(
            ReportSectionService service, ReportSectionConfig rsConfig, Report r) {
        return service.createReportSection("This is a response", rsConfig, r);
    }

    Report createTestEmployerReport(ReportService service, Coop c, EmployerContact ec, ReportConfig rc) {
        Report r = new Report();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));

            r = service.createReport(ReportStatus.COMPLETED, c, "Offer Letter", ec, multipartFile, rc);
            return r;
        } catch (IOException e) {
            return null;
        }
    }

    ReportConfig createTestReportConfig(ReportConfigService service, String type, Set<CourseOffering> courseOfferings) {
        return service.createReportConfig(true, 14, true, type, courseOfferings);
    }

    ReportSectionConfig createTestReportSectionConfig(
            ReportConfigService rcService,
            ReportSectionConfigService rscService,
            ReportConfig reportConfig) {
        return rscService.createReportSectionConfig(
                "How was your co-op?", ReportResponseType.LONG_TEXT, 1, reportConfig);
    }
}
