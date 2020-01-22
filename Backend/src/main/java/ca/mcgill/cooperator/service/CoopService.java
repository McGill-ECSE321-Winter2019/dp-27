package ca.mcgill.cooperator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.StudentReport;

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
        c.setEmployerReports(new ArrayList<EmployerReport>());
        c.setStudentReports(new ArrayList<StudentReport>());

        coopRepository.save(c);

		List<Coop> studentCoops = s.getCoops();
		if(studentCoops == null) {
			studentCoops = new ArrayList<>();
		}
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
	public Coop updateCoop(Coop coop, CoopStatus coopStatus, CourseOffering courseOffering, CoopDetails coopDetails) {
		if(coop == null) {
			throw new IllegalArgumentException("Coop cannot be null. ");
		}
		
		Coop c = coopRepository.findById(coop.getId()).orElse(null);
		if(c == null) {
			throw new IllegalArgumentException("Coop does not exist.");
		}
		
		if(coopStatus != null) {
			c.setStatus(coopStatus);
		}
		if(courseOffering != null) {
			c.setCourseOffering(courseOffering);
		}
		if(coopDetails != null) {
			c.setCoopDetails(coopDetails);
		}

		coopRepository.save(c);
		
		Student s = coop.getStudent();

		List<Coop> studentCoops = s.getCoops();
		studentCoops.remove(coop);
		studentCoops.add(c);
        s.setCoops(studentCoops);

		List<Coop> coops = courseOffering.getCoops();
		coops.remove(coop);
        coops.add(c);
        courseOffering.setCoops(coops);

        studentRepository.save(s);
		courseOfferingRepository.save(courseOffering);
		
		return coop;
		
	}
	
	@Transactional
	public Coop getCoop(Integer id) {
		if(id == null || id < 0) {
			throw new IllegalArgumentException("ID is invalid.");
		}
		Coop c = coopRepository.findById(id).orElse(null);
		if(c == null) {
			throw new IllegalArgumentException("Coop with that ID does not exist.");
		}
		return c;
	}

	 @Transactional
	 public List<Coop> getllCoops(){
	 	return ServiceUtils.toList(coopRepository.findAll());
	 }
	
	@Transactional
	public List<Coop> getCoopsByStatus(CoopStatus status) {
		if(status == null) {
			throw new IllegalArgumentException("Status cannot be null.");
		}
		List<Coop> coops = coopRepository.findByStatus(status);
		return coops;
	}

	@Transactional
	public Coop deleteCoop(Coop coop){
		if(coop == null){
			throw new IllegalArgumentException("Coop does not exist");
		}
		Coop c = coopRepository.findById(coop.getId()).orElse(null);
		if(c == null) {
			throw new IllegalArgumentException("Coop with that ID does not exist.");
		}
		coopRepository.delete(c);

		Student s = coop.getStudent();

		List<Coop> studentCoops = s.getCoops();
		studentCoops.remove(coop);
		s.setCoops(studentCoops);

		CourseOffering courseOffering = coop.getCourseOffering();

        List<Coop> coops = courseOffering.getCoops();
        coops.remove(coop);
        courseOffering.setCoops(coops);

        studentRepository.save(s);
		courseOfferingRepository.save(courseOffering);
		
		return c;
	}
}
