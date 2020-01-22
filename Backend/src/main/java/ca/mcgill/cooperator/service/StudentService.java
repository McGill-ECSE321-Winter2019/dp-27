package ca.mcgill.cooperator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
    
    /**
     * create new student in database
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @return newly created student
     */
	@Transactional
	public Student createStudent(String firstName, String lastName, String email, String studentID) {
		StringBuilder error = new StringBuilder();
		if(firstName == null || firstName.trim().length() == 0) {
			error.append("FirstName is null or invalid. ");
		}
		if(lastName == null || lastName.trim().length() == 0) {
			error.append("LastName is null or invalid. ");
		}
		if(!ServiceUtils.isValidEmail(email)) {
			error.append("Email is null or invalid. ");
		}
		if(studentID == null || studentID.trim().length() != 9) {
			error.append("StudentID is null or invalid. ");
		}
		if(error.length() != 0) {
			throw new IllegalArgumentException(error.toString().trim());
		}
		Student s = new Student();
		s.setFirstName(firstName);
		s.setLastName(lastName);
		s.setEmail(email);
		s.setStudentId(studentID);
		studentRepository.save(s);
		return s;
	}
    
        /**
     * create new student in database
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @param coops
     * @param notifications
     * @return newly created student
     */
	@Transactional
	public Student createStudent(String firstName, String lastName, String email, String studentID, List<Coop> coops, List<Notification> notifications) {
		StringBuilder error = new StringBuilder();
		if(firstName == null || firstName.trim().length() == 0) {
			error.append("FirstName is null or invalid. ");
		}
		if(lastName == null || lastName.trim().length() == 0) {
			error.append("LastName is null or invalid. ");
		}
		if(!ServiceUtils.isValidEmail(email)) {
			error.append("Email is null or invalid. ");
		}
		if(studentID == null || studentID.trim().length() != 9) {
			error.append("StudentID is null or invalid. ");
		}
		if(coops == null ) {
			error.append("List of Coops is null. ");
		}
		if(notifications == null) {
			error.append("List of Notifications is null. ");
		}
		if(error.length() != 0) {
			throw new IllegalArgumentException(error.toString().trim());
		}
		Student s = new Student();
		s.setFirstName(firstName);
		s.setLastName(lastName);
		s.setEmail(email);
		s.setStudentId(studentID);
		s.setCoops(coops);
		studentRepository.save(s);
		return s;
	}
	
	public Student updateStudent(Student student, String firstName,String lastName, String email, 
								 String studentID, List<Coop> coops, List<Notification> notifications) {
		if(student == null) {
			throw new IllegalArgumentException("Student cannot be null.");
		}
		
		Student s = studentRepository.findById(student.getId()).orElse(null);
		if(s == null) {
			throw new IllegalArgumentException("Student does not exist.");
		}
		
		if(firstName != null && firstName.trim().length() != 0) {
			s.setFirstName(firstName);
		}
		if(lastName != null && lastName.trim().length() != 0) {
			s.setLastName(lastName);
		}
		if(email != null && email.trim().length() != 0) {
			s.setEmail(email);
		}
		if(studentID != null && studentID.trim().length() == 9) {
			s.setStudentId(studentID);
		}
		if(coops != null) {
			s.setCoops(coops);
		}
		if(notifications != null) {
			s.setNotifications(notifications);
		}
		studentRepository.save(s);
		return s;
	}
	
	@Transactional 
	public List<Student> getStudentByFirstAndLast(String firstName, String lastName) {
		StringBuilder error = new StringBuilder();
		if(firstName == null || firstName.trim().length() == 0) {
			error.append("FirstName is null or invalid. ");
		}
		if(lastName == null || lastName.trim().length() == 0) {
			error.append("LastName is null or invalid. ");
		}
		if(error.length() != 0) {
			throw new IllegalArgumentException(error.toString().trim());
		}
		List<Student> s = studentRepository.findByFirstNameAndLastName(firstName, lastName);
		if(s == null) {
			throw new IllegalArgumentException("No students exist with that first and last name.");
		}
		return s;
	}
	
	@Transactional
	public List<Student> getStudentByFistName(String firstName){
		if(firstName == null || firstName.trim().length() == 0) {
			throw new IllegalArgumentException("FirstName is null or invalid. ");
		}

		List<Student> s = studentRepository.findByFirstName(firstName);
		if(s == null) {
			throw new IllegalArgumentException("No students exist with that first name.");
		}
		return s;
	}
	
	@Transactional
	public List<Student> getStudentByLastName(String lastName){
		if(lastName == null || lastName.trim().length() == 0) {
			throw new IllegalArgumentException("FirstName is null or invalid. ");
		}

		List<Student> s = studentRepository.findByLastName(lastName);
		if(s == null) {
			throw new IllegalArgumentException("No students exist with that first name.");
		}
		return s;
	}
	
	@Transactional 
	public Student getStudentByStudentID(String id) {
		if(id == null || id.trim().length() != 9) {
			throw new IllegalArgumentException("ID is invalid.");
		}
		Student s = studentRepository.findByStudentId(id);
		if(s == null) {
			throw new IllegalArgumentException("Student does not exist.");
		}
		return s;
	}	
	
	@Transactional 
	public Student getStudentByID(Integer id) {
		if(id == null || id < 0) {
			throw new IllegalArgumentException("ID is invalid.");
		}
		Student s = studentRepository.findById(id).orElse(null);
		if(s == null) {
			throw new IllegalArgumentException("Student does not exist.");
		}
		return s;
	}	
	
	@Transactional
	public Student deleteStudentByStudentID(String id) {
		if(id == null || id.trim().length() != 9) {
			throw new IllegalArgumentException("ID is invalid.");
		}
		Student s = studentRepository.findByStudentId(id);
		if(s == null) {
			throw new IllegalArgumentException("Student does not exist.");
		}
		studentRepository.delete(s);
		return s;
	}
	
}