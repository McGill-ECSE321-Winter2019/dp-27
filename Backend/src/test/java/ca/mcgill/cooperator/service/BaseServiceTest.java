package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

    Coop createTestCoop(CoopService service, CourseOffering co, Student s) {
        Coop coop = new Coop();
        coop = service.createCoop(CoopStatus.FUTURE, co, s);
        return coop;
    }

    CoopDetails createTestCoopDetails(CoopDetailsService service, EmployerContact ec, Coop c) {
        CoopDetails cd = new CoopDetails();
        cd = service.createCoopDetails(20, 40, ec, c);
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

    StudentReport createTestStudentReport(StudentReportService service, Coop c) {
        StudentReport sr = new StudentReport();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));

            sr =
                    service.createStudentReport(
                            ReportStatus.COMPLETED, c, "Offer Letter", multipartFile);
            return sr;
        } catch (IOException e) {
            return null;
        }
    }

    StudentReportSection createTestStudentReportSection(
            StudentReportSectionService service, ReportSectionConfig rsConfig, StudentReport sr) {
        return service.createReportSection("This is a response", rsConfig, sr);
    }

    EmployerReport createTestEmployerReport(
            EmployerReportService service, Coop c, EmployerContact ec) {
        EmployerReport er = new EmployerReport();
        File file = new File("src/test/resources/Test_Offer_Letter.pdf");
        try {
            MultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream(file));

            er =
                    service.createEmployerReport(
                            ReportStatus.COMPLETED, c, "Offer Letter", ec, multipartFile);
            return er;
        } catch (IOException e) {
            return null;
        }
    }

    EmployerReportSection createTestEmployerReportSection(
            EmployerReportSectionService service, ReportSectionConfig rsConfig, EmployerReport er) {
        return service.createReportSection("This is a response", rsConfig, er);
    }

    ReportSectionConfig createTestReportSectionConfig(
            ReportConfigService rcService, ReportSectionConfigService rscService) {
        ReportConfig reportConfig = rcService.createReportConfig(true, 14, true, "Evaluation");

        return rscService.createReportSectionConfig(
                "How was your co-op?", ReportResponseType.LONG_TEXT, reportConfig);
    }
}
