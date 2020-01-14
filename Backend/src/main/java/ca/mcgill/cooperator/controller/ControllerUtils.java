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
import java.util.ArrayList;
import java.util.List;

public class ControllerUtils {

    AdminDto convertToDto(Admin a) {
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

    List<AdminDto> convertAdminListToDto(List<Admin> admins) {
        List<AdminDto> adminDtos = new ArrayList<>(admins.size());

        for (Admin a : admins) {
            if (a == null) {
                throw new IllegalArgumentException("Admin does not exist!");
            }
            adminDtos.add(convertToDto(a));
        }

        return adminDtos;
    }

    CompanyDto convertToDto(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company does not exist!");
        }
        return new CompanyDto(
                c.getId(), c.getName(), convertEmployerContactListToDto(c.getEmployees()));
    }

    List<CompanyDto> convertCompanyListToDto(List<Company> companies) {
        List<CompanyDto> companyDtos = new ArrayList<>(companies.size());

        for (Company c : companies) {
            if (c == null) {
                throw new IllegalArgumentException("Company does not exist!");
            }
            companyDtos.add(convertToDto(c));
        }
        return companyDtos;
    }

    CoopDto convertToDto(Coop c) {
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

    List<CoopDto> convertCoopListToDto(List<Coop> coops) {
        List<CoopDto> coopDtos = new ArrayList<>(coops.size());

        for (Coop c : coops) {
            if (c == null) {
                throw new IllegalArgumentException("Coop does not exist!");
            }
            coopDtos.add(convertToDto(c));
        }
        return coopDtos;
    }

    CoopDetailsDto convertToDto(CoopDetails cd) {
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

    List<CoopDetailsDto> convertCoopDetailsListToDto(List<CoopDetails> coopDetails) {
        List<CoopDetailsDto> coopDetailsDtos = new ArrayList<>(coopDetails.size());

        for (CoopDetails cd : coopDetails) {
            if (cd == null) {
                throw new IllegalArgumentException("Coop details do not exist!");
            }
            coopDetailsDtos.add(convertToDto(cd));
        }
        return coopDetailsDtos;
    }

    CourseDto convertToDto(Course c) {
        if (c == null) {
            throw new IllegalArgumentException("Course does not exist!");
        }
        return new CourseDto(
                c.getId(), c.getName(), convertCourseOfferingListToDto(c.getCourseOfferings()));
    }

    List<CourseDto> convertCourseListToDto(List<Course> courses) {
        List<CourseDto> courseDtos = new ArrayList<>(courses.size());

        for (Course c : courses) {
            if (c == null) {
                throw new IllegalArgumentException("Course does not exist!");
            }
            courseDtos.add(convertToDto(c));
        }
        return courseDtos;
    }

    CourseOfferingDto convertToDto(CourseOffering co) {
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

    List<CourseOfferingDto> convertCourseOfferingListToDto(List<CourseOffering> courseOfferings) {
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<>(courseOfferings.size());

        for (CourseOffering co : courseOfferings) {
            if (co == null) {
                throw new IllegalArgumentException("Course Offering does not exist!");
            }
            courseOfferingDtos.add(convertToDto(co));
        }
        return courseOfferingDtos;
    }

    EmployerContactDto convertToDto(EmployerContact e) {
        if (e == null) {
            throw new IllegalArgumentException("Employer Contact does not exist!");
        }
        return new EmployerContactDto(
                e.getId(),
                e.getEmail(),
                e.getFirstName(),
                e.getLastName(),
                e.getPhoneNumber(),
                convertToDto(e.getCompany()),
                convertCoopDetailsListToDto(e.getCoopDetails()),
                convertEmployerReportListToDto(e.getEmployerReports()));
    }

    List<EmployerContactDto> convertEmployerContactListToDto(
            List<EmployerContact> employerContacts) {
        List<EmployerContactDto> employerContactDtos = new ArrayList<>(employerContacts.size());

        for (EmployerContact ec : employerContacts) {
            if (ec == null) {
                throw new IllegalArgumentException("Employer Contact does not exist!");
            }
            employerContactDtos.add(convertToDto(ec));
        }

        return employerContactDtos;
    }

    EmployerReportDto convertToDto(EmployerReport er) {
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

    List<EmployerReportDto> convertEmployerReportListToDto(List<EmployerReport> employerReports) {
        List<EmployerReportDto> employerReportDtos = new ArrayList<>(employerReports.size());

        for (EmployerReport er : employerReports) {
            if (er == null) {
                throw new IllegalArgumentException("Employer Report does not exist!");
            }
            employerReportDtos.add(convertToDto(er));
        }

        return employerReportDtos;
    }

    ReportSectionDto convertToDto(ReportSection rs) {
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

    List<ReportSectionDto> convertReportSectionListToDto(List<ReportSection> reportSections) {
        List<ReportSectionDto> reportSectionDtos = new ArrayList<>(reportSections.size());

        for (ReportSection rs : reportSections) {
            if (rs == null) {
                throw new IllegalArgumentException("Report section does not exist!");
            }
            reportSectionDtos.add(convertToDto(rs));
        }
        return reportSectionDtos;
    }

    NotificationDto convertToDto(Notification n) {
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

    List<NotificationDto> convertNotifListToDto(List<Notification> notifs) {
        List<NotificationDto> notifDtos = new ArrayList<>(notifs.size());

        for (Notification n : notifs) {
            if (n == null) {
                throw new IllegalArgumentException("Notification does not exist!");
            }
            notifDtos.add(convertToDto(n));
        }
        return notifDtos;
    }

    StudentDto convertToDto(Student s) {
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

    List<StudentDto> convertToDto(List<Student> students) {
        List<StudentDto> studentDtos = new ArrayList<>(students.size());

        for (Student s : students) {
            if (s == null) {
                throw new IllegalArgumentException("Student does not exist!");
            }
            studentDtos.add(convertToDto(s));
        }
        return studentDtos;
    }

    StudentReportDto convertToDto(StudentReport sr) {
        if (sr == null) {
            throw new IllegalArgumentException("Student Report does not exist!");
        }
        return new StudentReportDto(
                sr.getId(),
                sr.getStatus(),
                convertToDto(sr.getCoop()),
                convertReportSectionListToDto(sr.getReportSections()));
    }

    List<StudentReportDto> convertStudentReportListToDto(List<StudentReport> studentReports) {
        List<StudentReportDto> studentReportDtos = new ArrayList<>(studentReports.size());

        for (StudentReport sr : studentReports) {
            if (sr == null) {
                throw new IllegalArgumentException("Student Report does not exist!");
            }
            studentReportDtos.add(convertToDto(sr));
        }
        return studentReportDtos;
    }
}
