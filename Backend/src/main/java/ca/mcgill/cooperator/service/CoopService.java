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
public class CoopService extends BaseService {

    @Autowired CoopRepository coopRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired EmployerContactRepository employerContactRepository;

    /**
     * Creates a new Coop
     *
     * @param status
     * @param courseOffering
     * @param student
     * @return created Coop
     */
    @Transactional
    public Coop createCoop(CoopStatus status, CourseOffering courseOffering, Student student) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Co-op Status cannot be null. ");
        }
        if (courseOffering == null) {
            error.append("Course Offering cannot be null. ");
        }
        if (student == null) {
            error.append("Student cannot be null.");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Coop c = new Coop();
        c.setStatus(status);
        c.setCourseOffering(courseOffering);
        c.setStudent(student);
        c.setEmployerReports(new HashSet<EmployerReport>());
        c.setStudentReports(new HashSet<StudentReport>());

        return coopRepository.save(c);
    }

    /**
     * Retrieves a Coop by ID
     *
     * @param id
     * @return Coop with specified ID
     */
    @Transactional
    public Coop getCoopById(int id) {
        Coop c = coopRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Co-op with ID " + id + " does not exist!");
        }

        return c;
    }

    /**
     * Gets all Coops with specified status
     *
     * @param status
     * @return all Coops with the specified status
     */
    @Transactional
    public List<Coop> getCoopsByStatus(CoopStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Status cannot be null.");
        }
        List<Coop> coops = coopRepository.findByStatus(status);
        return coops;
    }

    /**
     * Gets all Coops
     *
     * @return all Coops
     */
    @Transactional
    public List<Coop> getAllCoops() {
        return ServiceUtils.toList(coopRepository.findAll());
    }

    /**
     * Gets all Coops for the specified Student
     *
     * @param student
     * @return all Coops for specified Student
     */
    @Transactional
    public List<Coop> getAllCoopsByStudent(Student student) {
        return ServiceUtils.toList(coopRepository.findByStudent(student));
    }

    /**
     * Gets all Coops for the specified CourseOffering
     *
     * @param courseOffering
     * @return all Coops for the specified CourseOffering
     */
    @Transactional
    public List<Coop> getAllCoopsForCourseOffering(CourseOffering courseOffering) {
        return ServiceUtils.toList(coopRepository.findByCourseOffering(courseOffering));
    }

    /**
     * Updates an existing Coop
     *
     * @param coop
     * @param status
     * @param courseOffering
     * @param student
     * @param coopDetails
     * @param employerReports
     * @param studentReports
     * @return the updated Coop
     */
    @Transactional
    public Coop updateCoop(
            Coop coop,
            CoopStatus status,
            CourseOffering courseOffering,
            Student student,
            CoopDetails coopDetails,
            Set<EmployerReport> employerReports,
            Set<StudentReport> studentReports) {
        StringBuilder error = new StringBuilder();
        if (coop == null) {
            error.append("Co-op to update cannot be null! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (status != null) {
            coop.setStatus(status);
        }
        if (courseOffering != null) {
            coop.setCourseOffering(courseOffering);
        }
        if (student != null) {
            coop.setStudent(student);
        }
        if (coopDetails != null) {
            coop.setCoopDetails(coopDetails);
        }
        if (employerReports != null) {
            coop.setEmployerReports(employerReports);
        }
        if (studentReports != null) {
            coop.setStudentReports(studentReports);
        }

        return coopRepository.save(coop);
    }

    /**
     * Deletes an existing Coop
     *
     * @param coop
     * @return the deleted Coop
     */
    @Transactional
    public Coop deleteCoop(Coop coop) {
        if (coop == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Co-op to delete cannot be null!");
        }

        Student s = coop.getStudent();
        Set<Coop> studentCoops = s.getCoops();
        studentCoops.remove(coop);
        s.setCoops(studentCoops);
        studentRepository.save(s);

        CourseOffering courseOffering = coop.getCourseOffering();
        List<Coop> courseOfferingCoops = courseOffering.getCoops();
        courseOfferingCoops.remove(coop);
        courseOffering.setCoops(courseOfferingCoops);
        courseOfferingRepository.save(courseOffering);

        CoopDetails coopDetails = coop.getCoopDetails();
        if (coopDetails != null) {
            coopDetails.setCoop(null);
            coopDetailsRepository.save(coopDetails);

            coop.setCoopDetails(null);
            coopRepository.save(coop);

            EmployerContact ec = coopDetails.getEmployerContact();
            Set<CoopDetails> details = ec.getCoopDetails();
            details.remove(coopDetails);
            ec.setCoopDetails(details);

            employerContactRepository.save(ec);
            coopDetailsRepository.delete(coopDetails);
        }
        coopRepository.delete(coop);

        return coop;
    }
}
