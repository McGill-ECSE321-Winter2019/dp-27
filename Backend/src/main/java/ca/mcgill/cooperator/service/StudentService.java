package ca.mcgill.cooperator.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

@Service
public class StudentService {
	@Autowired StudentRepository studentRepository;
	
	/**
	 * create new student in database
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param studentId
	 * @return newly created student
	 */
	@Transactional
	public Student createStudent(String firstName, String lastName, String email, String studentId) {
		StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("Student first name cannot be empty! ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Student last name cannot be empty! ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Student email cannot be empty! ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Student email must be a valid email!");
        }
        if (studentId == null || studentId.trim().length() == 0) {
            error.append("Student ID cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Student s = new Student();
        s.setFirstName(firstName.trim());
        s.setLastName(lastName.trim());
        s.setEmail(email.trim());
        s.setNotifications(new ArrayList<Notification>());
        s.setCoops(new ArrayList<Coop>());

        return studentRepository.save(s);
	}
}
