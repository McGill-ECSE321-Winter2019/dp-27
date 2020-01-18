package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.service.NotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

public class ControllerUtils {

    @Autowired private static NotificationService notificationService;

    /*
     * Domain Object to DTO conversion methods
     */

    static AdminDto convertToDto(Admin a) {
        if (a == null) {
            throw new IllegalArgumentException("Admin does not exist!");
        }
        return new AdminDto(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getEmail(),
                convertNotifListToDto(a.getSentNotifications()));
    }

    static List<AdminDto> convertAdminListToDto(List<Admin> admins) {
        List<AdminDto> adminDtos = new ArrayList<AdminDto>();

        for (Admin a : admins) {
            if (a == null) {
                throw new IllegalArgumentException("Admin does not exist!");
            }
            adminDtos.add(convertToDto(a));
        }

        return adminDtos;
    }

    static CompanyDto convertToDto(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company does not exist!");
        }

        // create company dto with no employer contacts
        CompanyDto companyDto = new CompanyDto(c.getId(), c.getName(), null);

        // create employer contact dtos with no company
        List<EmployerContact> employerContacts = c.getEmployees();
        List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();
        for (EmployerContact employerContact : employerContacts) {
            EmployerContactDto employerContactDto =
                    new EmployerContactDto(
                            employerContact.getId(),
                            employerContact.getEmail(),
                            employerContact.getFirstName(),
                            employerContact.getLastName(),
                            employerContact.getPhoneNumber(),
                            null,
                            convertCoopDetailsListToDto(employerContact.getCoopDetails()),
                            convertEmployerReportListToDto(employerContact.getEmployerReports()));
            employerContactDtos.add(employerContactDto);
        }

        companyDto.setEmployees(employerContactDtos);

        return companyDto;
    }

    static List<CompanyDto> convertCompanyListToDto(List<Company> companies) {
        List<CompanyDto> companyDtos = new ArrayList<CompanyDto>();

        for (Company c : companies) {
            if (c == null) {
                throw new IllegalArgumentException("Company does not exist!");
            }
            companyDtos.add(convertToDto(c));
        }
        return companyDtos;
    }

    static CoopDto convertToDto(Coop c) {
        if (c == null) {
            throw new IllegalArgumentException("Coop does not exist!");
        }
        return new CoopDto(
                c.getId(),
                c.getStatus(),
                convertToDto(c.getCourseOffering()),
                convertToDto(c.getCoopDetails()),
                convertToDto(c.getStudent()),
                convertStudentReportListToDto(c.getStudentReports()),
                convertEmployerReportListToDto(c.getEmployerReports()));
    }

    static List<CoopDto> convertCoopListToDto(Set<Coop> coops) {
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();

        for (Coop c : coops) {
            if (c == null) {
                throw new IllegalArgumentException("Coop does not exist!");
            }
            coopDtos.add(convertToDto(c));
        }
        return coopDtos;
    }

    static List<CoopDto> convertCoopListToDto(List<Coop> coops) {
        List<CoopDto> coopDtos = new ArrayList<CoopDto>();

        for (Coop c : coops) {
            if (c == null) {
                throw new IllegalArgumentException("Coop does not exist!");
            }
            coopDtos.add(convertToDto(c));
        }
        return coopDtos;
    }

    static CoopDetailsDto convertToDto(CoopDetails cd) {
        if (cd == null) {
            throw new IllegalArgumentException("Coop details do not exist!");
        }
        return new CoopDetailsDto(
                cd.getId(),
                cd.getPayPerHour(),
                cd.getHoursPerWeek(),
                convertToDto(cd.getEmployerContact()),
                convertToDto(cd.getCoop()));
    }

    static List<CoopDetailsDto> convertCoopDetailsListToDto(Set<CoopDetails> coopDetails) {
        List<CoopDetailsDto> coopDetailsDtos = new ArrayList<CoopDetailsDto>();

        for (CoopDetails cd : coopDetails) {
            if (cd == null) {
                throw new IllegalArgumentException("Coop details do not exist!");
            }
            coopDetailsDtos.add(convertToDto(cd));
        }
        return coopDetailsDtos;
    }

    static CourseDto convertToDto(Course c) {
        if (c == null) {
            throw new IllegalArgumentException("Course does not exist!");
        }
        return new CourseDto(
                c.getId(), c.getName(), convertCourseOfferingListToDto(c.getCourseOfferings()));
    }

    static List<CourseDto> convertCourseListToDto(List<Course> courses) {
        List<CourseDto> courseDtos = new ArrayList<CourseDto>();

        for (Course c : courses) {
            if (c == null) {
                throw new IllegalArgumentException("Course does not exist!");
            }
            courseDtos.add(convertToDto(c));
        }
        return courseDtos;
    }

    static CourseOfferingDto convertToDto(CourseOffering co) {
        if (co == null) {
            throw new IllegalArgumentException("Course Offering does not exist!");
        }
        return new CourseOfferingDto(
                co.getId(),
                co.getYear(),
                co.getSeason(),
                convertToDto(co.getCourse()),
                convertCoopListToDto(co.getCoops()));
    }

    static List<CourseOfferingDto> convertCourseOfferingListToDto(
            List<CourseOffering> courseOfferings) {
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<CourseOfferingDto>();

        for (CourseOffering co : courseOfferings) {
            if (co == null) {
                throw new IllegalArgumentException("Course Offering does not exist!");
            }
            courseOfferingDtos.add(convertToDto(co));
        }
        return courseOfferingDtos;
    }

    static EmployerContactDto convertToDto(EmployerContact e) {
        if (e == null) {
            throw new IllegalArgumentException("Employer Contact does not exist!");
        }

        // create employer contact dto with no company
        EmployerContactDto employerContactDto =
                new EmployerContactDto(
                        e.getId(),
                        e.getEmail(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPhoneNumber(),
                        null,
                        convertCoopDetailsListToDto(e.getCoopDetails()),
                        convertEmployerReportListToDto(e.getEmployerReports()));

        // create company dto manually with created employer contact dto
        Company company = e.getCompany();
        List<EmployerContact> employerContacts = company.getEmployees();
        List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();
        for (EmployerContact employerContact : employerContacts) {
            EmployerContactDto tempDto =
                    new EmployerContactDto(
                            employerContact.getId(),
                            employerContact.getEmail(),
                            employerContact.getFirstName(),
                            employerContact.getLastName(),
                            employerContact.getPhoneNumber(),
                            null,
                            convertCoopDetailsListToDto(employerContact.getCoopDetails()),
                            convertEmployerReportListToDto(employerContact.getEmployerReports()));
            employerContactDtos.add(tempDto);
        }

        CompanyDto companyDto =
                new CompanyDto(company.getId(), company.getName(), employerContactDtos);

        companyDto.setEmployees(employerContactDtos);
        employerContactDto.setCompany(companyDto);

        return employerContactDto;
    }

    static List<EmployerContactDto> convertEmployerContactListToDto(
            List<EmployerContact> employerContacts) {
    	
	    List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();
	    if (employerContacts != null) {
	        for (EmployerContact ec : employerContacts) {
	            if (ec == null) {
	                throw new IllegalArgumentException("Employer Contact does not exist!");
	            }
	            employerContactDtos.add(convertToDto(ec));
	        }
    	}
	    
        return employerContactDtos;
    }

    static EmployerReportDto convertToDto(EmployerReport er) {
        if (er == null) {
            throw new IllegalArgumentException("Employer Report does not exist!");
        }
        return new EmployerReportDto(
                er.getId(),
                er.getStatus(),
                convertToDto(er.getCoop()),
                convertToDto(er.getEmployerContact()),
                convertReportSectionListToDto(er.getReportSections()));
    }

    static List<EmployerReportDto> convertEmployerReportListToDto(
            Set<EmployerReport> employerReports) {
        List<EmployerReportDto> employerReportDtos = new ArrayList<EmployerReportDto>();

        if (employerReports != null && employerReports.size() > 0) {
            for (EmployerReport er : employerReports) {
                if (er == null) {
                    throw new IllegalArgumentException("Employer Report does not exist!");
                }
                employerReportDtos.add(convertToDto(er));
            }
        }

        return employerReportDtos;
    }

    static ReportSectionDto convertToDto(ReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException("Report section does not exist!");
        }
        return new ReportSectionDto(
                rs.getId(),
                rs.getTitle(),
                rs.getContent(),
                convertToDto(rs.getStudentReport()),
                convertToDto(rs.getEmployerReport()));
    }

    static List<ReportSectionDto> convertReportSectionListToDto(
            List<ReportSection> reportSections) {
        List<ReportSectionDto> reportSectionDtos = new ArrayList<ReportSectionDto>();

        for (ReportSection rs : reportSections) {
            if (rs == null) {
                throw new IllegalArgumentException("Report section does not exist!");
            }
            reportSectionDtos.add(convertToDto(rs));
        }
        return reportSectionDtos;
    }

    static NotificationDto convertToDto(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException("Notification does not exist!");
        }
        return new NotificationDto(
                n.getId(),
                n.getTitle(),
                n.getBody(),
                convertToDto(n.getStudent()),
                convertToDto(n.getSender()));
    }

    static List<NotificationDto> convertNotifListToDto(List<Notification> notifs) {
        List<NotificationDto> notifDtos = new ArrayList<NotificationDto>();

        for (Notification n : notifs) {
            if (n == null) {
                throw new IllegalArgumentException("Notification does not exist!");
            }
            notifDtos.add(convertToDto(n));
        }
        return notifDtos;
    }

    static List<NotificationDto> convertNotifListToDto(Set<Notification> notifs) {
        List<NotificationDto> notifDtos = new ArrayList<NotificationDto>();

        for (Notification n : notifs) {
            if (n == null) {
                throw new IllegalArgumentException("Notification does not exist!");
            }
            notifDtos.add(convertToDto(n));
        }
        return notifDtos;
    }

    static StudentDto convertToDto(Student s) {
        if (s == null) {
            throw new IllegalArgumentException("Student does not exist!");
        }
        return new StudentDto(
                s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getStudentId(),
                convertCoopListToDto(s.getCoops()),
                convertNotifListToDto(s.getNotifications()));
    }

    static List<StudentDto> convertToDto(List<Student> students) {
        List<StudentDto> studentDtos = new ArrayList<StudentDto>();

        for (Student s : students) {
            if (s == null) {
                throw new IllegalArgumentException("Student does not exist!");
            }
            studentDtos.add(convertToDto(s));
        }
        return studentDtos;
    }

    static StudentReportDto convertToDto(StudentReport sr) {
        if (sr == null) {
            throw new IllegalArgumentException("Student Report does not exist!");
        }
        return new StudentReportDto(
                sr.getId(),
                sr.getStatus(),
                convertToDto(sr.getCoop()),
                convertReportSectionListToDto(sr.getReportSections()));
    }

    static List<StudentReportDto> convertStudentReportListToDto(Set<StudentReport> studentReports) {
        List<StudentReportDto> studentReportDtos = new ArrayList<StudentReportDto>();

        for (StudentReport sr : studentReports) {
            if (sr == null) {
                throw new IllegalArgumentException("Student Report does not exist!");
            }
            studentReportDtos.add(convertToDto(sr));
        }
        return studentReportDtos;
    }

    /*
     * DTO to Domain Object conversion methods
     */

    static List<Notification> convertNotificationListToDomainObject(
            List<NotificationDto> notifDtos) {
        List<Notification> notifs = new ArrayList<Notification>();
        for (NotificationDto nDto : notifDtos) {
            Notification n = notificationService.getNotification(nDto.getId());
            notifs.add(n);
        }
        return notifs;
    }
}
