package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.EmployerReportSectionDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.dto.StudentReportSectionDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.EmployerReportSectionService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControllerUtils {

    static final String ERROR_PREFIX = "ERROR [DTO Conversion]: ";

    /*
     * Domain Object to DTO conversion methods
     */

    public static AdminDto convertToDto(Admin a) {
        if (a == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Admin does not exist!");
        }

        AdminDto adminDto =
                new AdminDto(a.getId(), a.getFirstName(), a.getLastName(), a.getEmail(), null);

        List<Notification> notifications = a.getSentNotifications();
        List<NotificationDto> notificationDtos = new ArrayList<NotificationDto>();
        if (notifications != null) {
            for (Notification notification : notifications) {
                NotificationDto notificationDto =
                        new NotificationDto(
                                notification.getId(),
                                notification.getTitle(),
                                notification.getBody(),
                                null, // null student
                                null, // null admin
                                notification.getSeen(),
                                notification.getTimeStamp());
                Student student = notification.getStudent();
                StudentDto studentDto =
                        new StudentDto(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail(),
                                student.getStudentId(),
                                null, // null coops, if need coop information look up specific
                                // student by id
                                null); // null notifs
                notificationDto.setStudent(studentDto);
                notificationDtos.add(notificationDto);
            }
        }

        adminDto.setSentNotifications(notificationDtos);

        return adminDto;
    }

    public static List<AdminDto> convertAdminListToDto(List<Admin> admins) {
        List<AdminDto> adminDtos = new ArrayList<AdminDto>();

        for (Admin a : admins) {
            if (a == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Admin does not exist!");
            }
            adminDtos.add(convertToDto(a));
        }

        return adminDtos;
    }

    public static CompanyDto convertToDto(Company c) {
        if (c == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Company does not exist!");
        }

        CompanyDto companyDto =
                new CompanyDto(
                        c.getId(), c.getName(), c.getCity(), c.getRegion(), c.getCountry(), null);

        // create employer contact dtos
        List<EmployerContact> employerContacts = c.getEmployees();
        List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();
        if (employerContacts != null) {
            for (EmployerContact employerContact : employerContacts) {
                EmployerContactDto employerContactDto =
                        new EmployerContactDto(
                                employerContact.getId(),
                                employerContact.getEmail(),
                                employerContact.getFirstName(),
                                employerContact.getLastName(),
                                employerContact.getPhoneNumber(),
                                null, // null company, company is parent so no need for this
                                // information to be repeated
                                null, // null list of coop details, look up employer contact by id
                                // to get coop details
                                null); // null employer reports, look up employer contact by id to
                // get all employer reports

                employerContactDtos.add(employerContactDto);
            }
        }
        companyDto.setEmployees(employerContactDtos);

        return companyDto;
    }

    public static List<CompanyDto> convertCompanyListToDto(List<Company> companies) {
        List<CompanyDto> companyDtos = new ArrayList<CompanyDto>();

        for (Company c : companies) {
            if (c == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Company does not exist!");
            }
            companyDtos.add(convertToDto(c));
        }
        return companyDtos;
    }

    public static CoopDto convertToDto(Coop c) {
        if (c == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Coop does not exist!");
        }

        // first make coop with null course offering and null student
        CoopDto coopDto =
                new CoopDto(
                        c.getId(),
                        c.getStatus(),
                        null, // null course offering
                        null, // null coop details
                        null, // null student
                        null, // null student reports
                        null); // null employer reports

        // create course offering dto with null coop dtos
        CourseOffering courseOffering = c.getCourseOffering();
        CourseOfferingDto courseOfferingDto =
                new CourseOfferingDto(
                        courseOffering.getId(),
                        courseOffering.getYear(),
                        courseOffering.getSeason(),
                        null, // null course
                        null); // null coops
        Course course = courseOffering.getCourse();
        CourseDto courseDto =
                new CourseDto(course.getId(), course.getName(), null); // null course offering
        courseOfferingDto.setCourse(courseDto);

        coopDto.setCourseOffering(courseOfferingDto);

        // create coop details dto
        CoopDetails coopDetails = c.getCoopDetails();
        if (coopDetails != null) {
            CoopDetailsDto coopDetailsDto =
                    new CoopDetailsDto(
                            coopDetails.getId(),
                            coopDetails.getPayPerHour(),
                            coopDetails.getHoursPerWeek(),
                            null, // null employer contact
                            null); // null coop since coop is parent
            EmployerContact employerContact = coopDetails.getEmployerContact();
            EmployerContactDto employerContactDto =
                    new EmployerContactDto(
                            employerContact.getId(),
                            employerContact.getEmail(),
                            employerContact.getFirstName(),
                            employerContact.getLastName(),
                            employerContact.getPhoneNumber(),
                            null, // null company
                            null, // null coop details since parent
                            null); // null employer reports, look up employer contact by id if
            // needed

            Company company = employerContact.getCompany();
            CompanyDto companyDto =
                    new CompanyDto(
                            company.getId(),
                            company.getName(),
                            company.getCity(),
                            company.getRegion(),
                            company.getCountry(),
                            null); // null employees, look up specific company for all employees
            employerContactDto.setCompany(companyDto);

            coopDetailsDto.setEmployerContact(employerContactDto);

            coopDto.setCoopDetails(coopDetailsDto);
        }

        // create student dto
        Student student = c.getStudent();
        StudentDto studentDto =
                new StudentDto(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getStudentId(),
                        null, // set coops to null since coop is parent
                        null); // set notifications to null, look up student by id to get all
        // notifications
        coopDto.setStudent(studentDto);

        // create student report dtos
        Set<StudentReport> studentReports = c.getStudentReports();
        if (studentReports != null) {
            List<StudentReportDto> studentReportDtos = new ArrayList<StudentReportDto>();
            for (StudentReport studentReport : studentReports) {
                StudentReportDto studentReportDto =
                        new StudentReportDto(
                                studentReport.getId(),
                                studentReport.getStatus(),
                                studentReport.getTitle(),
                                studentReport.getData(),
                                null, // null coop since coop is parent
                                null); // null report sections

                Set<StudentReportSection> reportSections = studentReport.getReportSections();
                List<StudentReportSectionDto> reportSectionDtos =
                        new ArrayList<StudentReportSectionDto>();
                for (StudentReportSection reportSection : reportSections) {
                    StudentReportSectionDto reportSectionDto =
                            new StudentReportSectionDto(
                                    reportSection.getId(),
                                    reportSection.getResponse(),
                                    null, // null student report since parent
                                    null); // null employer report since part of student report
                    reportSectionDtos.add(reportSectionDto);
                }
                studentReportDto.setReportSections(reportSectionDtos);
                studentReportDtos.add(studentReportDto);
            }
            coopDto.setStudentReports(studentReportDtos);
        }

        // create employer report dtos
        Set<EmployerReport> employerReports = c.getEmployerReports();
        if (employerReports != null) {
            List<EmployerReportDto> employerReportDtos = new ArrayList<EmployerReportDto>();
            for (EmployerReport employerReport : employerReports) {
                EmployerReportDto employerReportDto =
                        new EmployerReportDto(
                                employerReport.getId(),
                                employerReport.getTitle(),
                                employerReport.getStatus(),
                                employerReport.getData(),
                                null, // null coop since coop is parent
                                null, // null employer contact since can find this info from coop
                                // details
                                null); // null report sections

                Set<EmployerReportSection> reportSections = employerReport.getReportSections();
                List<EmployerReportSectionDto> reportSectionDtos =
                        new ArrayList<EmployerReportSectionDto>();
                for (EmployerReportSection reportSection : reportSections) {
                    EmployerReportSectionDto reportSectionDto =
                            new EmployerReportSectionDto(
                                    reportSection.getId(),
                                    reportSection.getResponse(),
                                    null, // null student report since part of employer report
                                    null); // null employer report since parent
                    reportSectionDtos.add(reportSectionDto);
                }
                employerReportDto.setReportSections(reportSectionDtos);
                employerReportDtos.add(employerReportDto);
            }
            coopDto.setEmployerReports(employerReportDtos);
        }

        return coopDto;
    }

    public static List<CoopDto> convertCoopListToDto(Set<Coop> coops) {
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();

        for (Coop c : coops) {
            if (c == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Coop does not exist!");
            }
            coopDtos.add(convertToDto(c));
        }
        return coopDtos;
    }

    public static List<CoopDto> convertCoopListToDto(List<Coop> coops) {
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();

        for (Coop c : coops) {
            if (c == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Coop does not exist!");
            }
            coopDtos.add(convertToDto(c));
        }
        return coopDtos;
    }

    public static CoopDetailsDto convertToDto(CoopDetails cd) {
        if (cd == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Coop details do not exist!");
        }

        CoopDetailsDto coopDetailsDto =
                new CoopDetailsDto(
                        cd.getId(),
                        cd.getPayPerHour(),
                        cd.getHoursPerWeek(),
                        null, // null employer contact
                        null); // null coop

        // create employer contact dto
        EmployerContact employerContact = cd.getEmployerContact();
        EmployerContactDto employerContactDto =
                new EmployerContactDto(
                        employerContact.getId(),
                        employerContact.getEmail(),
                        employerContact.getFirstName(),
                        employerContact.getLastName(),
                        employerContact.getPhoneNumber(),
                        null, // null company
                        null, // null coop details since parent
                        null); // null employer reports, look up employer by id to get reports

        Company company = employerContact.getCompany();
        CompanyDto companyDto =
                new CompanyDto(
                        company.getId(),
                        company.getName(),
                        company.getCity(),
                        company.getRegion(),
                        company.getCountry(),
                        null); // null employees, look up company by id to get employees

        employerContactDto.setCompany(companyDto);

        coopDetailsDto.setEmployerContact(employerContactDto);

        // create coop dto
        Coop coop = cd.getCoop();
        CoopDto coopDto =
                new CoopDto(
                        coop.getId(),
                        coop.getStatus(),
                        null, // null course offering
                        null, // null coop details since coop details is parent
                        null, // null student
                        null, // null student reports, look up coop by id to get student reports
                        null); // null employer reports, look up coop by id to get employer reports

        CourseOffering courseOffering = coop.getCourseOffering();
        CourseOfferingDto courseOfferingDto =
                new CourseOfferingDto(
                        courseOffering.getId(),
                        courseOffering.getYear(),
                        courseOffering.getSeason(),
                        null, // null course
                        null); // null coops since coop is parent
        Course course = courseOffering.getCourse();
        CourseDto courseDto =
                new CourseDto(
                        course.getId(),
                        course.getName(),
                        null); // null course offering since parent
        courseOfferingDto.setCourse(courseDto);

        coopDto.setCourseOffering(courseOfferingDto);

        Student student = coop.getStudent();
        StudentDto studentDto =
                new StudentDto(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getStudentId(),
                        null, // null coops since coop is parent
                        null); // null notifications, look up student by id to get notifications
        coopDto.setStudent(studentDto);

        coopDetailsDto.setCoop(coopDto);

        return coopDetailsDto;
    }

    public static List<CoopDetailsDto> convertCoopDetailsListToDto(
            Collection<CoopDetails> coopDetails) {
        List<CoopDetailsDto> coopDetailsDtos = new ArrayList<CoopDetailsDto>();

        for (CoopDetails cd : coopDetails) {
            if (cd == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Coop details do not exist!");
            }
            coopDetailsDtos.add(convertToDto(cd));
        }
        return coopDetailsDtos;
    }

    public static CourseDto convertToDto(Course c) {
        if (c == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Course does not exist!");
        }

        CourseDto courseDto = new CourseDto(c.getId(), c.getName(), null); // null course offerings

        // create course offering dtos
        List<CourseOffering> courseOfferings = c.getCourseOfferings();
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<CourseOfferingDto>();
        if (courseOfferings != null) {
            for (CourseOffering courseOffering : courseOfferings) {
                CourseOfferingDto courseOfferingDto =
                        new CourseOfferingDto(
                                courseOffering.getId(),
                                courseOffering.getYear(),
                                courseOffering.getSeason(),
                                null, // null course since parent
                                null); // null coops, look up course offering by id to get all coops
                courseOfferingDtos.add(courseOfferingDto);
            }
        }

        courseDto.setCourseOfferings(courseOfferingDtos);

        return courseDto;
    }

    public static List<CourseDto> convertCourseListToDto(List<Course> courses) {
        List<CourseDto> courseDtos = new ArrayList<CourseDto>();

        for (Course c : courses) {
            if (c == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Course does not exist!");
            }
            courseDtos.add(convertToDto(c));
        }
        return courseDtos;
    }

    public static CourseOfferingDto convertToDto(CourseOffering co) {
        if (co == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Course Offering does not exist!");
        }

        // create course offering dto with null course
        CourseOfferingDto courseOfferingDto =
                new CourseOfferingDto(
                        co.getId(),
                        co.getYear(),
                        co.getSeason(),
                        null, // null course
                        null); // null coops

        // create course dto
        Course course = co.getCourse();
        CourseDto courseDto =
                new CourseDto(
                        course.getId(),
                        course.getName(),
                        null); // null course offerings since parent
        courseOfferingDto.setCourse(courseDto);

        // create coop dtos
        List<Coop> coops = co.getCoops();
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();
        if (coops != null) {
            for (Coop coop : coops) {
                CoopDto coopDto =
                        new CoopDto(
                                coop.getId(),
                                coop.getStatus(),
                                null, // null course offering since parent
                                null, // null details
                                null, // null student
                                null, // null student reports, look up coop by id to get student
                                // reports
                                null); // null employer reports, look up coop by id to get employer
                // reports

                CoopDetails coopDetails = coop.getCoopDetails();
                if (coopDetails != null) {
                    CoopDetailsDto coopDetailsDto =
                            new CoopDetailsDto(
                                    coopDetails.getId(),
                                    coopDetails.getPayPerHour(),
                                    coopDetails.getHoursPerWeek(),
                                    null, // null employer contact, look up coop details by id to ge
                                    // employer contact
                                    null); // null coop since parent
                    coopDto.setCoopDetails(coopDetailsDto);
                } else {
                    coopDto.setCoopDetails(null);
                }

                Student student = coop.getStudent();
                StudentDto studentDto =
                        new StudentDto(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail(),
                                student.getStudentId(),
                                null, // null coops, look up student by id to get coops
                                null); // null notifications, look up student by id to get
                // notifications
                coopDto.setStudent(studentDto);

                coopDtos.add(coopDto);
            }
        }
        courseOfferingDto.setCoops(coopDtos);

        return courseOfferingDto;
    }

    public static List<CourseOfferingDto> convertCourseOfferingListToDto(
            List<CourseOffering> courseOfferings) {
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<CourseOfferingDto>();

        for (CourseOffering co : courseOfferings) {
            if (co == null) {
                throw new IllegalArgumentException(
                        ERROR_PREFIX + "Course Offering does not exist!");
            }
            courseOfferingDtos.add(convertToDto(co));
        }
        return courseOfferingDtos;
    }

    public static EmployerContactDto convertToDto(EmployerContact e) {
        if (e == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Employer Contact does not exist!");
        }

        // create employer contact dto
        EmployerContactDto employerContactDto =
                new EmployerContactDto(
                        e.getId(),
                        e.getEmail(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPhoneNumber(),
                        null, // null company
                        null, // null coop details
                        null); // null employer reports

        // create company dto
        Company company = e.getCompany();
        CompanyDto companyDto =
                new CompanyDto(
                        company.getId(),
                        company.getName(),
                        company.getCity(),
                        company.getRegion(),
                        company.getCountry(),
                        null); // null employees, look up company by id to get all employees

        employerContactDto.setCompany(companyDto);

        // create coop details dtos
        Set<CoopDetails> coopDetails = e.getCoopDetails();
        List<CoopDetailsDto> coopDetailsDtos = new ArrayList<CoopDetailsDto>();
        if (coopDetails != null) {
            for (CoopDetails coopDetail : coopDetails) {
                CoopDetailsDto coopDetailsDto =
                        new CoopDetailsDto(
                                coopDetail.getId(),
                                coopDetail.getPayPerHour(),
                                coopDetail.getHoursPerWeek(),
                                null, // null employer contact since parent
                                null); // null coop, look up coop details by id to get coop

                coopDetailsDtos.add(coopDetailsDto);
            }
        }
        employerContactDto.setCoopDetails(coopDetailsDtos);

        // create employer report dtos
        Set<EmployerReport> employerReports = e.getEmployerReports();
        List<EmployerReportDto> employerReportDtos = new ArrayList<EmployerReportDto>();
        if (employerReports != null) {
            for (EmployerReport employerReport : employerReports) {
                EmployerReportDto employerReportDto =
                        new EmployerReportDto(
                                employerReport.getId(),
                                employerReport.getTitle(),
                                employerReport.getStatus(),
                                employerReport.getData(),
                                null, // null coop
                                null, // null employer contact since parent
                                null); // null report sections, look up employer report by id to get
                // sections

                Coop coop = employerReport.getCoop();
                CoopDto coopDto =
                        new CoopDto(
                                coop.getId(),
                                coop.getStatus(),
                                null, // null course offering, look up coop by id to get course
                                // offering
                                null, // null coop details, look up coop by id to get coop details
                                null, // null student
                                null, // null student reports, look up coop by id to get student
                                // reports
                                null); // null employer reports, look up coop by id to get all
                // employer reports

                Student student = coop.getStudent();
                StudentDto studentDto =
                        new StudentDto(
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail(),
                                student.getStudentId(),
                                null, // null coops, look up student by id to get all coops
                                null); // null notifications, look up student by id to get all
                // notifications

                coopDto.setStudent(studentDto);

                employerReportDto.setCoop(coopDto);

                employerReportDtos.add(employerReportDto);
            }
        }

        employerContactDto.setEmployerReports(employerReportDtos);

        return employerContactDto;
    }

    public static List<EmployerContactDto> convertEmployerContactListToDto(
            List<EmployerContact> employerContacts) {

        List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();

        if (employerContacts != null) {
            for (EmployerContact ec : employerContacts) {
                if (ec == null) {
                    throw new IllegalArgumentException(
                            ERROR_PREFIX + "Employer Contact does not exist!");
                }
                employerContactDtos.add(convertToDto(ec));
            }
        }
        return employerContactDtos;
    }

    public static EmployerReportDto convertToDto(EmployerReport er) {
        if (er == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Employer Report does not exist!");
        }

        EmployerReportDto employerReportDto =
                new EmployerReportDto(
                        er.getId(),
                        er.getTitle(),
                        er.getStatus(),
                        er.getData(),
                        null, // null coop
                        null, // null employer contact
                        null); // null report sections

        // create coop dto
        Coop coop = er.getCoop();
        CoopDto coopDto =
                new CoopDto(
                        coop.getId(),
                        coop.getStatus(),
                        null, // null course offering, look up coop by id to get course offering
                        null, // null coop details, look up coop by id to get coop details
                        null, // null student
                        null, // null student reports, look up coop by id to get student reports
                        null); // null employer reports, look up coop by id to get all employer
        // reports

        Student student = coop.getStudent();
        StudentDto studentDto =
                new StudentDto(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getStudentId(),
                        null, // null coops, look up student by id to get all coops
                        null); // null notifications, look up student by id to get all notifications

        coopDto.setStudent(studentDto);

        employerReportDto.setCoop(coopDto);

        // create employer contact dto
        EmployerContact employerContact = er.getEmployerContact();
        EmployerContactDto employerContactDto =
                new EmployerContactDto(
                        employerContact.getId(),
                        employerContact.getEmail(),
                        employerContact.getFirstName(),
                        employerContact.getLastName(),
                        employerContact.getPhoneNumber(),
                        null, // null company
                        null, // null coop details, loop up employer contact by id to get coop
                        // details
                        null); // null employer reports, look up employer contact by id to get all
        // employer reports

        Company company = employerContact.getCompany();
        CompanyDto companyDto =
                new CompanyDto(
                        company.getId(),
                        company.getName(),
                        company.getCity(),
                        company.getRegion(),
                        company.getCountry(),
                        null); // null employees, look up company by id to get employees

        employerContactDto.setCompany(companyDto);

        employerReportDto.setEmployerContact(employerContactDto);

        // create employer report section dtos
        Set<EmployerReportSection> reportSections = er.getReportSections();
        List<EmployerReportSectionDto> reportSectionDtos =
                new ArrayList<EmployerReportSectionDto>();
        if (reportSections != null) {
            for (EmployerReportSection reportSection : reportSections) {
                EmployerReportSectionDto reportSectionDto =
                        new EmployerReportSectionDto(
                                reportSection.getId(),
                                reportSection.getResponse(),
                                null, // null student report since part of employe report
                                null); // null employer report since parent
                reportSectionDtos.add(reportSectionDto);
            }
        }

        employerReportDto.setReportSections(reportSectionDtos);

        return employerReportDto;
    }

    public static List<EmployerReportDto> convertEmployerReportListToDto(
            Set<EmployerReport> employerReports) {
        List<EmployerReportDto> employerReportDtos = new ArrayList<EmployerReportDto>();

        if (employerReports != null && employerReports.size() > 0) {
            for (EmployerReport er : employerReports) {
                if (er == null) {
                    throw new IllegalArgumentException(
                            ERROR_PREFIX + "Employer Report does not exist!");
                }
                employerReportDtos.add(convertToDto(er));
            }
        }

        return employerReportDtos;
    }

    public static StudentReportSectionDto convertToDto(StudentReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student report section does not exist!");
        }
        return new StudentReportSectionDto(
                rs.getId(),
                rs.getResponse(),
                null, // ignore StudentReport
                null); // ignore ReportSectionConfig
    }

    public static List<StudentReportSectionDto> convertStudentReportSectionListToDto(
            List<StudentReportSection> reportSections) {
        List<StudentReportSectionDto> reportSectionDtos = new ArrayList<StudentReportSectionDto>();

        for (StudentReportSection rs : reportSections) {
            if (rs == null) {
                throw new IllegalArgumentException(
                        ERROR_PREFIX + "Student report section does not exist!");
            }
            reportSectionDtos.add(convertToDto(rs));
        }
        return reportSectionDtos;
    }

    public static EmployerReportSectionDto convertToDto(EmployerReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer report section does not exist!");
        }
        return new EmployerReportSectionDto(
                rs.getId(),
                rs.getResponse(),
                null, // ignore EmployerReport
                null); // ignore ReportSectionConfig
    }

    public static List<EmployerReportSectionDto> convertEmployerReportSectionListToDto(
            List<EmployerReportSection> reportSections) {
        List<EmployerReportSectionDto> reportSectionDtos =
                new ArrayList<EmployerReportSectionDto>();

        for (EmployerReportSection rs : reportSections) {
            if (rs == null) {
                throw new IllegalArgumentException(
                        ERROR_PREFIX + "Employer report section does not exist!");
            }
            reportSectionDtos.add(convertToDto(rs));
        }
        return reportSectionDtos;
    }

    public static NotificationDto convertToDto(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Notification does not exist!");
        }

        NotificationDto notificationDto =
                new NotificationDto(
                        n.getId(),
                        n.getTitle(),
                        n.getBody(),
                        null, // null student
                        null, // null admin
                        n.getSeen(),
                        n.getTimeStamp());

        // create student dto
        Student student = n.getStudent();
        StudentDto studentDto =
                new StudentDto(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getStudentId(),
                        null, // null coops, look up student by id to get coops
                        null); // null notifications, look up student by id to get all notifications

        notificationDto.setStudent(studentDto);

        // create admin dto
        Admin admin = n.getSender();
        AdminDto adminDto =
                new AdminDto(
                        admin.getId(),
                        admin.getFirstName(),
                        admin.getLastName(),
                        admin.getEmail(),
                        null); // null notifications, look up admin by id to see all notifications
        // sent

        notificationDto.setSender(adminDto);

        return notificationDto;
    }

    public static List<NotificationDto> convertNotifListToDto(List<Notification> notifs) {
        List<NotificationDto> notifDtos = new ArrayList<NotificationDto>();

        for (Notification n : notifs) {
            if (n == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Notification does not exist!");
            }
            notifDtos.add(convertToDto(n));
        }
        return notifDtos;
    }

    public static List<NotificationDto> convertNotifListToDto(Set<Notification> notifs) {
        List<NotificationDto> notifDtos = new ArrayList<NotificationDto>();

        for (Notification n : notifs) {
            if (n == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Notification does not exist!");
            }
            notifDtos.add(convertToDto(n));
        }
        return notifDtos;
    }

    public static StudentDto convertToDto(Student s) {
        if (s == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist!");
        }

        StudentDto studentDto =
                new StudentDto(
                        s.getId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getEmail(),
                        s.getStudentId(),
                        null, // null coops
                        null); // null notifications

        // create coop dtos
        Set<Coop> coops = s.getCoops();
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();
        if (coops != null) {
            for (Coop coop : coops) {
                CoopDto coopDto =
                        new CoopDto(
                                coop.getId(),
                                coop.getStatus(),
                                null, // null course offering
                                null, // null coop details
                                null, // null student since parent
                                null, // null student reports, look up coop by id to get student
                                // reports
                                null); // null employer reports, look up coop by id to get employer
                // reports

                CourseOffering courseOffering = coop.getCourseOffering();
                CourseOfferingDto courseOfferingDto =
                        new CourseOfferingDto(
                                courseOffering.getId(),
                                courseOffering.getYear(),
                                courseOffering.getSeason(),
                                null, // null course, look up course offering by id to get course
                                null); // null coops, look up course offering to get all coops

                coopDto.setCourseOffering(courseOfferingDto);

                CoopDetails coopDetails = coop.getCoopDetails();
                if (coopDetails != null) {
                    CoopDetailsDto coopDetailsDto =
                            new CoopDetailsDto(
                                    coopDetails.getId(),
                                    coopDetails.getPayPerHour(),
                                    coopDetails.getHoursPerWeek(),
                                    null, // null employer contact, look up coop details by id to
                                    // get employer contact
                                    null); // null coop since parent

                    coopDto.setCoopDetails(coopDetailsDto);
                }

                coopDtos.add(coopDto);
            }
        }

        studentDto.setCoops(coopDtos);

        // create notification dtos
        Set<Notification> notifications = s.getNotifications();
        List<NotificationDto> notificationDtos = new ArrayList<NotificationDto>();
        if (notifications != null) {
            for (Notification notification : notifications) {
                NotificationDto notificationDto =
                        new NotificationDto(
                                notification.getId(),
                                notification.getTitle(),
                                notification.getBody(),
                                null, // null student since parent
                                null, // null admin
                                notification.getSeen(),
                                notification.getTimeStamp());

                Admin admin = notification.getSender();
                AdminDto adminDto =
                        new AdminDto(
                                admin.getId(),
                                admin.getFirstName(),
                                admin.getLastName(),
                                admin.getEmail(),
                                null); // null sent notifications, look up admin by id to get all
                // notifications sent

                notificationDto.setSender(adminDto);
                notificationDtos.add(notificationDto);
            }
        }

        studentDto.setNotifications(notificationDtos);

        return studentDto;
    }

    public static List<StudentDto> convertToDto(List<Student> students) {
        List<StudentDto> studentDtos = new ArrayList<StudentDto>();

        for (Student s : students) {
            if (s == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist!");
            }
            studentDtos.add(convertToDto(s));
        }
        return studentDtos;
    }

    public static StudentReportDto convertToDto(StudentReport sr) {
        if (sr == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student Report does not exist!");
        }

        StudentReportDto studentReportDto =
                new StudentReportDto(
                        sr.getId(),
                        sr.getStatus(),
                        sr.getTitle(),
                        sr.getData(),
                        null, // null coop
                        null); // null report sections

        // create coop dto
        Coop coop = sr.getCoop();
        CoopDto coopDto =
                new CoopDto(
                        coop.getId(),
                        coop.getStatus(),
                        null, // null course offering, look up coop by id to get course section
                        null, // null coop details, look up coop by id to get coop details
                        null, // null student
                        null, // null student reports, look up coop by to get all student reports
                        null); // null employer reports, look up coop by id to get all employer
        // reports

        Student student = coop.getStudent();
        StudentDto studentDto =
                new StudentDto(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getStudentId(),
                        null, // null coops, look up student by id to get all coops
                        null); // null notifications, look up student by id to get all notifications

        coopDto.setStudent(studentDto);
        studentReportDto.setCoop(coopDto);

        // create report section dtos
        Set<StudentReportSection> reportSections = sr.getReportSections();
        List<StudentReportSectionDto> reportSectionDtos = new ArrayList<StudentReportSectionDto>();
        if (reportSections != null) {
            for (StudentReportSection reportSection : reportSections) {
                StudentReportSectionDto reportSectionDto =
                        new StudentReportSectionDto(
                                reportSection.getId(),
                                reportSection.getResponse(),
                                null, // null StudentReport since parent
                                null); // null ReportSectionConfig since parent
                reportSectionDtos.add(reportSectionDto);
            }
        }

        studentReportDto.setReportSections(reportSectionDtos);

        return studentReportDto;
    }

    public static List<StudentReportDto> convertStudentReportListToDto(
            Set<StudentReport> studentReports) {
        List<StudentReportDto> studentReportDtos = new ArrayList<StudentReportDto>();

        for (StudentReport sr : studentReports) {
            if (sr == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Student Report does not exist!");
            }
            studentReportDtos.add(convertToDto(sr));
        }
        return studentReportDtos;
    }

    public static ReportConfigDto convertToDto(ReportConfig rc) {
        if (rc == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Report config cannot be null!");
        }
        return new ReportConfigDto(
                rc.getId(),
                rc.getRequiresFile(),
                rc.getDeadline(),
                rc.getIsDeadlineFromStart(),
                rc.getType(),
                convertReportSectionConfigListToDto(rc.getReportSectionConfigs()));
    }

    public static List<ReportConfigDto> convertReportConfigListToDto(
            Collection<ReportConfig> reportConfigs) {
        List<ReportConfigDto> reportConfigDtos = new ArrayList<>();
        for (ReportConfig reportConfig : reportConfigs) {
            if (reportConfig == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Report config cannot be null!");
            }
            reportConfigDtos.add(convertToDto(reportConfig));
        }
        return reportConfigDtos;
    }

    public static ReportSectionConfigDto convertToDto(ReportSectionConfig rsConfig) {
        if (rsConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section config cannot be null!");
        }
        return new ReportSectionConfigDto(
                rsConfig.getId(),
                rsConfig.getSectionPrompt(),
                rsConfig.getResponseType(),
                null,
                null,
                null);
    }

    public static List<ReportSectionConfigDto> convertReportSectionConfigListToDto(
            Collection<ReportSectionConfig> rsConfigs) {
        List<ReportSectionConfigDto> rsConfigDtos = new ArrayList<>();
        for (ReportSectionConfig rsConfig : rsConfigs) {
            if (rsConfig == null) {
                throw new IllegalArgumentException(
                        ERROR_PREFIX + "Report section config cannot be null!");
            }
            rsConfigDtos.add(convertToDto(rsConfig));
        }
        return rsConfigDtos;
    }

    /*
     * DTO to Domain Object conversion methods
     */

    public static List<Notification> convertNotificationListToDomainObject(
            NotificationService service, List<NotificationDto> notifDtos) {
        List<Notification> notifs = new ArrayList<Notification>();
        for (NotificationDto nDto : notifDtos) {
            Notification n = service.getNotification(nDto.getId());
            notifs.add(n);
        }
        return notifs;
    }

    public static Set<Notification> convertNotificationListToDomainObjectSet(
            NotificationService service, List<NotificationDto> notifDtos) {
        Set<Notification> notifs = new HashSet<>();
        if (notifDtos == null) {
            return notifs;
        }
        for (NotificationDto nDto : notifDtos) {
            Notification n = service.getNotification(nDto.getId());
            notifs.add(n);
        }
        return notifs;
    }

    public static Set<StudentReportSection> convertStudentReportSectionsToDomainObjects(
            StudentReportSectionService service, Collection<StudentReportSectionDto> rsDtos) {
        Set<StudentReportSection> reports = new HashSet<StudentReportSection>();
        for (StudentReportSectionDto rsDto : rsDtos) {
            StudentReportSection rs = service.getReportSection(rsDto.getId());
            reports.add(rs);
        }
        return reports;
    }

    public static Set<EmployerReportSection> convertEmployerReportSectionsToDomainObjects(
            EmployerReportSectionService service, Collection<EmployerReportSectionDto> rsDtos) {
        Set<EmployerReportSection> reports = new HashSet<EmployerReportSection>();
        for (EmployerReportSectionDto rsDto : rsDtos) {
            EmployerReportSection rs = service.getReportSection(rsDto.getId());
            reports.add(rs);
        }
        return reports;
    }

    public static Set<Coop> convertCoopsListToDomainObject(
            CoopService service, List<CoopDto> coopDto) {
        Set<Coop> coops = new HashSet<>();
        if (coopDto == null) return coops;
        for (CoopDto cDto : coopDto) {
            Coop c = service.getCoopById(cDto.getId());
            coops.add(c);
        }
        return coops;
    }

    public static List<CourseOffering> convertCourseOfferingListToDomainObject(
            CourseOfferingService service, List<CourseOfferingDto> coDtos) {
        List<CourseOffering> cos = new ArrayList<>();
        for (CourseOfferingDto coDto : coDtos) {
            if (coDto != null) {
                CourseOffering co = service.getCourseOfferingById(coDto.getId());
                cos.add(co);
            }
        }
        return cos;
    }

    public static List<ReportSectionConfig> convertReportSectionConfigDtosToDomainObjects(
            ReportSectionConfigService service, Collection<ReportSectionConfigDto> rscDtos) {
        List<ReportSectionConfig> reportSectionConfigs = new ArrayList<>();
    	if (rscDtos == null) {
    		return reportSectionConfigs;
    	}
        for (ReportSectionConfigDto rscDto : rscDtos) {
            if (rscDto != null) {
                ReportSectionConfig rsc = service.getReportSectionConfig(rscDto.getId());
                reportSectionConfigs.add(rsc);
            }
        }
        return reportSectionConfigs;
    }
}
