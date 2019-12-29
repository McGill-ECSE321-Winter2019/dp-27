package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoopService {

    @Autowired CoopRepository coopRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

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
            error.append("Co-op Status cannot be null! ");
        }
        if (courseOffering == null) {
            error.append("Course Offering cannot be null! ");
        }
        if (s == null) {
            error.append("Student cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Coop c = new Coop();
        c.setStatus(status);
        c.setCourseOffering(courseOffering);
        c.setStudent(s);
        c.setEmployerReports(new ArrayList<EmployerReport>());
        c.setStudentReports(new ArrayList<StudentReport>());

        coopRepository.save(c);

        List<Coop> studentCoops = s.getCoops();
        studentCoops.add(c);
        s.setCoops(studentCoops);

        List<Coop> coops = courseOffering.getCoops();
        coops.add(c);
        courseOffering.setCoops(coops);

        studentRepository.save(s);
        courseOfferingRepository.save(courseOffering);

        return coopRepository.save(c);
    }
}
