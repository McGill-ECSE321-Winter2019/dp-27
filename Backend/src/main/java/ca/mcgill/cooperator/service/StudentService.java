package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService extends BaseService {
    @Autowired StudentRepository studentRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired NotificationRepository notificationRepository;

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
    public Student createStudent(
            String firstName, String lastName, String email, String studentId) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("Student first name cannot be empty. ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Student last name cannot be empty. ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Student email cannot be empty. ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Student email must be a valid email. ");
        }
        if (studentId == null || studentId.trim().length() != 9) {
            error.append("Student ID cannot be null or invalid.");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Student s = new Student();
        s.setFirstName(firstName.trim());
        s.setLastName(lastName.trim());
        s.setEmail(email.trim());
        s.setStudentId(studentId);
        s.setNotifications(new HashSet<Notification>());
        s.setCoops(new HashSet<Coop>());

        return studentRepository.save(s);
    }

    @Transactional
    public List<Student> getStudentByFirstAndLast(String firstName, String lastName) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("FirstName is null or invalid. ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("LastName is null or invalid. ");
        }
        if (error.length() != 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        List<Student> s = studentRepository.findByFirstNameAndLastName(firstName, lastName);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "No students exist with that first and last name.");
        }
        return s;
    }

    @Transactional
    public List<Student> getStudentByFirstName(String firstName) {
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "FirstName is null or invalid. ");
        }

        List<Student> s = studentRepository.findByFirstName(firstName);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "No students exist with that first name.");
        }
        return s;
    }

    @Transactional
    public List<Student> getStudentByLastName(String lastName) {
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "LastName is null or invalid. ");
        }

        List<Student> s = studentRepository.findByLastName(lastName);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "No students exist with that last name.");
        }
        return s;
    }

    @Transactional
    public Student getStudentByStudentID(String id) {
        if (id == null || id.trim().length() != 9) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student ID is invalid.");
        }
        Student s = studentRepository.findByStudentId(id);
        if (s == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist.");
        }
        return s;
    }

    @Transactional
    public Student getStudentByID(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "ID is invalid.");
        }
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist.");
        }
        return s;
    }

    @Transactional
    public Student getStudentById(int id) {
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student with ID " + id + " does not exist.");
        }

        return s;
    }

    @Transactional
    public Student getStudentByEmail(String email) {
        Student s = studentRepository.findByEmail(email);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student with email " + email + " does not exist.");
        }

        return s;
    }

    @Transactional
    public List<Student> getAllStudents() {
        return ServiceUtils.toList(studentRepository.findAll());
    }

    @Transactional
    public Student updateStudent(
            Student s,
            String firstName,
            String lastName,
            String email,
            String studentId,
            Set<Coop> coops,
            Set<Notification> notifs) {

        StringBuilder error = new StringBuilder();
        if (s == null) {
            error.append("Student to update cannot be null. ");
        }
        if (firstName != null && firstName.trim().length() == 0) {
            error.append("Student first name cannot be empty. ");
        }
        if (lastName != null && lastName.trim().length() == 0) {
            error.append("Student last name cannot be empty. ");
        }
        if (email != null && email.trim().length() == 0) {
            error.append("Student email cannot be empty. ");
        } else if (email != null && !ServiceUtils.isValidEmail(email)) {
            error.append("Student email is invalid. ");
        }
        if (studentId != null && studentId.trim().length() != 9) {
            error.append("Student ID is invalid.");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (firstName != null) {
            s.setFirstName(firstName.trim());
        }
        if (lastName != null) {
            s.setLastName(lastName.trim());
        }
        if (email != null) {
            s.setEmail(email.trim());
        }
        if (studentId != null) {
            s.setStudentId(studentId);
        }
        if (notifs != null) {
            s.setNotifications(notifs);
        }
        if (coops != null) {
            s.setCoops(coops);
        }

        return studentRepository.save(s);
    }

    @Transactional
    public Student deleteStudent(Student s) {
        if (s == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student to delete cannot be null.");
        }

        studentRepository.delete(s);
        return s;
    }

    @Transactional
    public Student deleteStudentByStudentID(String id) {
        if (id == null || id.trim().length() != 9) {
            throw new IllegalArgumentException(ERROR_PREFIX + "ID is invalid.");
        }
        Student s = studentRepository.findByStudentId(id);
        if (s == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist.");
        }
        studentRepository.delete(s);
        return s;
    }
}
