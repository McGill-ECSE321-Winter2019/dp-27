package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
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

        coopRepository.save(c);

        Set<Coop> studentCoops = s.getCoops();
        studentCoops.add(c);
        s.setCoops(studentCoops);

        List<Coop> coops = courseOffering.getCoops();
        coops.add(c);
        courseOffering.setCoops(coops);

        studentRepository.save(s);
        courseOfferingRepository.save(courseOffering);

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
        if (status == null) {
            error.append("Co-op Status cannot be null! ");
        }
        if (courseOffering == null) {
            error.append("Course Offering cannot be null! ");
        }
        if (s == null) {
            error.append("Student cannot be null! ");
        }
        if (cd == null) {
            error.append("Co-op Details cannot be null! ");
        }
        // cannot be null but can be empty
        if (employerReports == null) {
            error.append("Employer Reports cannot be null! ");
        }
        if (studentReports == null) {
            error.append("Student Reports cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        c.setStatus(status);
        c.setCourseOffering(courseOffering);
        c.setStudent(s);
        c.setCoopDetails(cd);
        c.setEmployerReports(employerReports);
        c.setStudentReports(studentReports);
        coopRepository.save(c);

        boolean studentContains = false;
        Set<Coop> studentCoops = s.getCoops();
        for (Coop studentCoop : studentCoops) {
            if (studentCoop.getId() == c.getId()) {
                studentCoops.remove(studentCoop);
                studentCoops.add(c);
                studentContains = true;
            }
        }
        if (studentContains == false) {
            studentCoops.add(c);
        }

        studentRepository.save(s);

        cd.setCoop(c);
        coopDetailsRepository.save(cd);

        for (EmployerReport employerReport : employerReports) {
            employerReport.setCoop(c);
            employerReportRepository.save(employerReport);
        }

        for (StudentReport studentReport : studentReports) {
            studentReport.setCoop(c);
            studentReportRepository.save(studentReport);
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

        coopRepository.delete(c);

        return c;
    }
}
