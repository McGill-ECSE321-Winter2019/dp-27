package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.AuthorDto;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Author;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.ReportSectionService;
import ca.mcgill.cooperator.service.ReportService;
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

        Set<Notification> notifications = a.getSentNotifications();
        Set<NotificationDto> notificationDtos = new HashSet<NotificationDto>();
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
        
        Set<Report> reports = a.getReports();
        Set<ReportDto> reportDtos = new HashSet<ReportDto>();
        if (reports != null) {
        	for (Report report : reports) {
        		ReportDto reportDto = new ReportDto(report.getId(),
        											report.getTitle(),
        											report.getStatus(),
        											report.getData(),
        											null, //null coop
        											null, //null author since admin is parent
        											null); //null report sections for now, could be changed later if admin starts to be authors of reports
        		reportDtos.add(reportDto);
        	}
        }

        adminDto.setReports(reportDtos);

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
                        null); // null reports

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
                            coopDetails.getStartDate(),
                            coopDetails.getEndDate(),
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
                            null); // null reports, look up employer contact by id if needed

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

        // create report dtos
        Set<Report> reports = c.getReports();
        List<ReportDto> reportDtos = new ArrayList<ReportDto>();
        if (reports != null) {
            for (Report report : reports) {
                ReportDto reportDto =
                        new ReportDto(
                        		report.getId(),
                        		report.getTitle(),
                        		report.getStatus(),
                        		report.getData(),
                                null, // null coop since coop is parent
                                null, // null author, look up report by id to get author
                                null); // null report sections
                
                Author author = report.getAuthor();
                AuthorDto authorDto = new AuthorDto();
                authorDto.setId(author.getId());
                authorDto.setFirstName(author.getEmail());
                authorDto.setLastName(author.getLastName());
                authorDto.setEmail(author.getEmail());
                authorDto.setReports(null); //null since parent
                
                reportDto.setAuthor(authorDto);

                Set<ReportSection> reportSections = report.getReportSections();
                List<ReportSectionDto> reportSectionDtos =
                        new ArrayList<ReportSectionDto>();
                for (ReportSection reportSection : reportSections) {
                    ReportSectionDto reportSectionDto =
                            new ReportSectionDto(
                                    reportSection.getId(),
                                    reportSection.getResponse(),
                                    null, // null report 
                                    null); // null report section config
                    reportSectionDtos.add(reportSectionDto);
                }
                reportDto.setReportSections(reportSectionDtos);
                reportDtos.add(reportDto);
            }
        }
        coopDto.setReports(reportDtos);

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
                        cd.getStartDate(),
                        cd.getEndDate(),
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
                        null); // null student reports, look up coop by id to get reports

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
                                null); // null reports, look up coop by id to get reports

                CoopDetails coopDetails = coop.getCoopDetails();
                if (coopDetails != null) {
                    CoopDetailsDto coopDetailsDto =
                            new CoopDetailsDto(
                                    coopDetails.getId(),
                                    coopDetails.getPayPerHour(),
                                    coopDetails.getHoursPerWeek(),
                                    coopDetails.getStartDate(),
                                    coopDetails.getEndDate(),
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

    public static Set<CourseOfferingDto> convertCourseOfferingSetToDto(
            Set<CourseOffering> courseOfferings) {
        Set<CourseOfferingDto> courseOfferingDtos = new HashSet<CourseOfferingDto>();

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
                        null); // null reports

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
                                coopDetail.getStartDate(),
                                coopDetail.getEndDate(),
                                null, // null employer contact since parent
                                null); // null coop, look up coop details by id to get coop

                coopDetailsDtos.add(coopDetailsDto);
            }
        }
        employerContactDto.setCoopDetails(coopDetailsDtos);

        // create employer report dtos
        Set<Report> reports = e.getReports();
        Set<ReportDto> reportDtos = new HashSet<ReportDto>();
        if (reports != null) {
            for (Report report : reports) {
                ReportDto reportDto =
                        new ReportDto(
                        		report.getId(),
                        		report.getTitle(),
                        		report.getStatus(),
                        		report.getData(),
                                null, // null coop
                                null, // null author since parent
                                null); // null report sections, look up report by id to get sections

                Coop coop = report.getCoop();
                CoopDto coopDto =
                        new CoopDto(
                                coop.getId(),
                                coop.getStatus(),
                                null, // null course offering, look up coop by id to get course
                                // offering
                                null, // null coop details, look up coop by id to get coop details
                                null, // null student
                                null); // null reports, look up coop by id to get reports

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

                reportDto.setCoop(coopDto);

                reportDtos.add(reportDto);
            }
        }

        employerContactDto.setReports(reportDtos);

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

    public static ReportDto convertToDto(Report r) {
        if (r == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Employer Report does not exist!");
        }

        ReportDto reportDto =
                new ReportDto(
                        r.getId(),
                        r.getTitle(),
                        r.getStatus(),
                        r.getData(),
                        null, // null coop
                        null, // null author
                        null); // null report sections

        // create coop dto
        Coop coop = r.getCoop();
        CoopDto coopDto =
                new CoopDto(
                        coop.getId(),
                        coop.getStatus(),
                        null, // null course offering, look up coop by id to get course offering
                        null, // null coop details, look up coop by id to get coop details
                        null, // null student
                        null); // null reports, look up coop by id to get reports
                        
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

        reportDto.setCoop(coopDto);

        // create author dto
        Author author = r.getAuthor();
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setEmail(author.getEmail());
        authorDto.setReports(null);

        reportDto.setAuthor(authorDto);

        // create report section dtos
        Set<ReportSection> reportSections = r.getReportSections();
        List<ReportSectionDto> reportSectionDtos =
                new ArrayList<ReportSectionDto>();
        if (reportSections != null) {
            for (ReportSection reportSection : reportSections) {
                ReportSectionDto reportSectionDto =
                        new ReportSectionDto(
                                reportSection.getId(),
                                reportSection.getResponse(),
                                null, // null student report since part of employe report
                                null); // null employer report since parent
                reportSectionDtos.add(reportSectionDto);
            }
        }

        reportDto.setReportSections(reportSectionDtos);

        return reportDto;
    }

    public static List<ReportDto> convertReportListToDto(
            Set<Report> reports) {
        List<ReportDto> reportDtos = new ArrayList<ReportDto>();

        if (reports != null && reports.size() > 0) {
            for (Report r : reports) {
                if (r == null) {
                    throw new IllegalArgumentException(
                            ERROR_PREFIX + "Report does not exist!");
                }
                reportDtos.add(convertToDto(r));
            }
        }

        return reportDtos;
    }
    
    public static List<ReportDto> convertReportListToDto(
            List<Report> reports) {
        List<ReportDto> reportDtos = new ArrayList<ReportDto>();

        if (reports != null && reports.size() > 0) {
            for (Report r : reports) {
                if (r == null) {
                    throw new IllegalArgumentException(
                            ERROR_PREFIX + "Report does not exist!");
                }
                reportDtos.add(convertToDto(r));
            }
        }

        return reportDtos;
    }

    public static ReportSectionDto convertToDto(ReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section does not exist!");
        }
        return new ReportSectionDto(
                rs.getId(),
                rs.getResponse(),
                null, // ignore EmployerReport
                null); // ignore ReportSectionConfig
    }

    public static List<ReportSectionDto> convertReportSectionListToDto(
            List<ReportSection> reportSections) {
        List<ReportSectionDto> reportSectionDtos =
                new ArrayList<ReportSectionDto>();

        for (ReportSection rs : reportSections) {
            if (rs == null) {
                throw new IllegalArgumentException(
                        ERROR_PREFIX + "Report section does not exist!");
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
        Set<CoopDto> coopDtos = new HashSet<CoopDto>();
        if (coops != null) {
            for (Coop coop : coops) {
                CoopDto coopDto =
                        new CoopDto(
                                coop.getId(),
                                coop.getStatus(),
                                null, // null course offering
                                null, // null coop details
                                null, // null student since parent
                                null); // null student reports, look up coop by id to get reports
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
                                    coopDetails.getStartDate(),
                                    coopDetails.getEndDate(),
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
        Set<NotificationDto> notificationDtos = new HashSet<NotificationDto>();
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
        
        Set<Report> reports = s.getReports();
        Set<ReportDto> reportDtos = new HashSet<ReportDto>();
        if (reports != null) {
        	for (Report report : reports) {
        		ReportDto reportDto = new ReportDto(report.getId(),
        											report.getTitle(),
        											report.getStatus(),
        											report.getData(),
        											null, //null coop
        											null, //null author since student is parent
        											null); //null report sections
        		
        		Set<ReportSection> reportSections = report.getReportSections();
        		List<ReportSectionDto> reportSectionDtos = new ArrayList<ReportSectionDto>();
        		for (ReportSection reportSection : reportSections) {
        			ReportSectionDto reportSectionDto = new ReportSectionDto(reportSection.getId(),
        																	 reportSection.getResponse(),
        																	 null, //null report since parent
        																	 null); //null report section config
        			
        			ReportSectionConfig reportSectionConfig = reportSection.getReportSectionConfig();
        			ReportSectionConfigDto reportSectionConfigDto = new ReportSectionConfigDto(
        																	reportSectionConfig.getId(),
        																	reportSectionConfig.getSectionPrompt(),
        																	reportSectionConfig.getResponseType(),
        																	reportSectionConfig.getQuestionNumber(),
        																	null, //null report config
        																	null); //null report sections
        			
        			reportSectionDto.setReportSectionConfig(reportSectionConfigDto);
        			reportSectionDtos.add(reportSectionDto);
        		}
        		reportDto.setReportSections(reportSectionDtos);
        		reportDtos.add(reportDto);
        	}
        }

        studentDto.setReports(reportDtos);
        
        

        studentDto.setNotifications(notificationDtos);

        return studentDto;
    }

    public static Set<StudentDto> convertStudentSetToDto(Set<Student> students) {
        Set<StudentDto> studentDtos = new HashSet<StudentDto>();

        for (Student s : students) {
            if (s == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist!");
            }
            studentDtos.add(convertToDto(s));
        }
        return studentDtos;
    }

    public static List<StudentDto> convertStudentListToDto(List<Student> students) {
        List<StudentDto> studentDtos = new ArrayList<StudentDto>();

        for (Student s : students) {
            if (s == null) {
                throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist!");
            }
            studentDtos.add(convertToDto(s));
        }
        return studentDtos;
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

        ReportConfig rc = rsConfig.getReportConfig();
        // set the ReportSectionConfigs to null so we don't get infinite recursion
        ReportConfigDto rcDto =
                new ReportConfigDto(
                        rc.getId(),
                        rc.getRequiresFile(),
                        rc.getDeadline(),
                        rc.getIsDeadlineFromStart(),
                        rc.getType(),
                        null);

        return new ReportSectionConfigDto(
                rsConfig.getId(),
                rsConfig.getSectionPrompt(),
                rsConfig.getResponseType(),
                rsConfig.getQuestionNumber(),
                rcDto,
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
            NotificationService service, Set<NotificationDto> notifDtos) {
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

    public static Set<ReportSection> convertReportSectionsToDomainObjects(
           ReportSectionService service, Collection<ReportSectionDto> rsDtos) {
        Set<ReportSection> reports = new HashSet<ReportSection>();
        for (ReportSectionDto rsDto : rsDtos) {
            ReportSection rs = service.getReportSection(rsDto.getId());
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
    
    public static Set<Coop> convertCoopsListToDomainObject(
            CoopService service, Set<CoopDto> coopDto) {
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

    public static List<EmployerContact> covertEmployerContactDtosToDomainObjects(
            EmployerContactService service, List<EmployerContactDto> employerContactDtos) {
        List<EmployerContact> employerContacts = new ArrayList<EmployerContact>();
        if (employerContactDtos == null) {
            return employerContacts;
        }

        for (EmployerContactDto employerContactDto : employerContactDtos) {
            if (employerContactDto != null) {
                EmployerContact employerContact =
                        service.getEmployerContact(employerContactDto.getId());
                employerContacts.add(employerContact);
            }
        }
        return employerContacts;
    }

    public static Set<Report> convertReportDtosToDomainObjects(
            ReportService service, Set<ReportDto> reportDtos) {
        Set<Report> reports = new HashSet<Report>();
        if (reportDtos == null) {
            return reports;
        }

        for (ReportDto reportDto : reportDtos) {
            Report report = service.getReport(reportDto.getId());
            reports.add(report);
        }

        return reports;
    }
    
    public static Set<Report> convertReportDtosToDomainObjects(
            ReportService service, List<ReportDto> reportDtos) {
        Set<Report> reports = new HashSet<Report>();
        if (reportDtos == null) {
            return reports;
        }

        for (ReportDto reportDto : reportDtos) {
            Report report = service.getReport(reportDto.getId());
            reports.add(report);
        }

        return reports;
    }

    public static Set<CoopDetails> convertCoopDetailsDtosToDomainObjects(
            CoopDetailsService service, List<CoopDetailsDto> coopDetailsDtos) {
        Set<CoopDetails> coopDetails = new HashSet<CoopDetails>();
        if (coopDetailsDtos == null) {
            return coopDetails;
        }

        for (CoopDetailsDto coopDetailsDto : coopDetailsDtos) {
            CoopDetails cd = service.getCoopDetails(coopDetailsDto.getId());
            coopDetails.add(cd);
        }

        return coopDetails;
    }
}
