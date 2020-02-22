package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoopService {

    @Autowired CoopRepository coopRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired EmployerContactRepository employerContactRepository;

    /**
     * create new coop in database
     *
     * @param status
     * @param courseOffering
     * @param s
     * @return created coop
     */
    @Transactional
    public Coop createCoop(CoopStatus status, CourseOffering courseOffering, Student s) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Co-op Status cannot be null. ");
        }
        if (courseOffering == null) {
            error.append("Course Offering cannot be null. ");
        }
        if (s == null) {
            error.append("Student cannot be null.");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Coop c = new Coop();
        c.setStatus(status);
        c.setCourseOffering(courseOffering);
        c.setStudent(s);
        c.setEmployerReports(new HashSet<EmployerReport>());
        c.setStudentReports(new HashSet<StudentReport>());

        return coopRepository.save(c);
    }

    @Transactional
    public Coop getCoopById(int id) {
        Coop c = coopRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Co-op with ID " + id + " does not exist!");
        }

        return c;
    }

    @Transactional
    public List<Coop> getAllCoops() {
        return ServiceUtils.toList(coopRepository.findAll());
    }

    @Transactional
    public List<Coop> getAllCoopsByStudent(Student s) {
        return ServiceUtils.toList(coopRepository.findByStudent(s));
    }

    @Transactional
    public Coop updateCoop(
            Coop c,
            CoopStatus status,
            CourseOffering courseOffering,
            Student s,
            CoopDetails cd,
            Set<EmployerReport> employerReports,
            Set<StudentReport> studentReports) {
        StringBuilder error = new StringBuilder();
        if (c == null) {
            error.append("Co-op to update cannot be null! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        if (status != null) {
            c.setStatus(status);
        }
        if (courseOffering != null) {
            c.setCourseOffering(courseOffering);
        }
        if (s != null) {
            c.setStudent(s);
        }
        if (cd != null) {
            c.setCoopDetails(cd);
        }
        if (employerReports != null) {
            c.setEmployerReports(employerReports);
        }
        if (studentReports != null) {
            c.setStudentReports(studentReports);
        }

        return coopRepository.save(c);
    }

    @Transactional
    public List<Coop> getCoopsByStatus(CoopStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        List<Coop> coops = coopRepository.findByStatus(status);
        return coops;
    }

    @Transactional
    public Coop deleteCoop(Coop c) {
        if (c == null) {
            throw new IllegalArgumentException("Co-op to delete cannot be null!");
        }

        Student s = c.getStudent();
        Set<Coop> studentCoops = s.getCoops();
        studentCoops.remove(c);
        s.setCoops(studentCoops);
        studentRepository.save(s);

        CourseOffering courseOffering = c.getCourseOffering();
        List<Coop> courseOfferingCoops = courseOffering.getCoops();
        courseOfferingCoops.remove(c);
        courseOffering.setCoops(courseOfferingCoops);
        courseOfferingRepository.save(courseOffering);

        CoopDetails coopDetails = c.getCoopDetails();
        if (coopDetails != null) {
            coopDetails.setCoop(null);
            coopDetailsRepository.save(coopDetails);
            c.setCoopDetails(null);
            coopRepository.save(c);
            EmployerContact ec = coopDetails.getEmployerContact();
            Set<CoopDetails> details = ec.getCoopDetails();
            details.remove(coopDetails);
            ec.setCoopDetails(details);
            employerContactRepository.save(ec);
            coopDetailsRepository.delete(coopDetails);
        }

        coopRepository.delete(c);

        return c;
    }
}
