package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.Season;
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
     * Creates a new Student
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @return the created Student
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
        s.setReports(new HashSet<Report>());

        return studentRepository.save(s);
    }

    /**
     * Gets all Students with the specified name
     *
     * @param firstName
     * @param lastName
     * @return all Students with specified name
     */
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

    /**
     * Gets all Students with the specified first name
     *
     * @param firstName
     * @return all Students with the specified first name
     */
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

    /**
     * Gets all Students with the specified last name
     *
     * @param lastName
     * @return all Students with the specified last name
     */
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

    /**
     * Gets a Student by student ID
     *
     * @param id
     * @return Student with specified student ID
     */
    @Transactional
    public Student getStudentByStudentId(String id) {
        if (id == null || id.trim().length() != 9) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student ID is invalid.");
        }
        Student s = studentRepository.findByStudentId(id);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student with ID " + id + " does not exist.");
        }
        return s;
    }

    /**
     * Gets a Student by ID
     *
     * @param id
     * @return Student with specified ID
     */
    @Transactional
    public Student getStudentById(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "ID is invalid.");
        }
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student with ID " + id + " does not exist.");
        }
        return s;
    }

    /**
     * Gets the most recent Coop for the specified Student
     *
     * @param student
     * @return most recent Coop for the specified Student, if any
     */
    @Transactional
    public Coop getMostRecentCoop(Student student) {
        if (student == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student does not exist.");
        }
        Coop mostRecent = null;
        for (Coop c : student.getCoops()) {
            if (mostRecent == null) {
                mostRecent = c;
            } else {
                // c is a bigger year, more recent
                if (mostRecent.getCourseOffering().getYear() < c.getCourseOffering().getYear()) {
                    mostRecent = c;
                }
                // same year, check seasons
                else if (mostRecent.getCourseOffering().getYear()
                        == c.getCourseOffering().getYear()) {
                    // if current mostRecent is Winter, update most recent to c
                    if (mostRecent.getCourseOffering().getSeason() == Season.WINTER) {
                        mostRecent = c;
                    }
                    // if current mostRecent is Summer, update most recent to c if c is Fall
                    else if (mostRecent.getCourseOffering().getSeason() == Season.SUMMER
                            && c.getCourseOffering().getSeason() == Season.FALL) {
                        mostRecent = c;
                    }
                }
            }
        }
        return mostRecent;
    }

    /**
     * Gets all Students that have done the specified number of Coops
     *
     * @param numCoops
     * @param status
     * @return all Students that have done the specified number of Coops
     */
    @Transactional
    public Set<Student> getStudentsByCourse(int numCoops, CoopStatus status) {
        if (numCoops < 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Please enter valid number of coops");
        }
        Set<Student> allStudents = ServiceUtils.toSet(studentRepository.findAll());
        Set<Student> studentsToReturn = ServiceUtils.toSet(studentRepository.findAll());
        for (Student s : allStudents) {
            int count = 0;
            if (status != null) {
                for (Coop c : s.getCoops()) {
                    if (c.getStatus() == status) count++;
                }
            } else {
                count = s.getCoops().size();
            }
            if (count == numCoops) studentsToReturn.add(s);
        }
        return studentsToReturn;
    }

    /**
     * Gets all Students
     *
     * @return all Students
     */
    @Transactional
    public List<Student> getAllStudents() {
        return ServiceUtils.toList(studentRepository.findAll());
    }

    /**
     * Gets all Students as a Set
     *
     * @return Set of all Students
     */
    @Transactional
    public Set<Student> getAllStudentsSet() {
        return ServiceUtils.toSet(studentRepository.findAll());
    }

    /**
     * Gets a Student by email
     *
     * @param email
     * @return Student with specified email
     */
    @Transactional
    public Student getStudentByEmail(String email) {
        Student s = studentRepository.findByEmail(email);
        if (s == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student with email " + email + " does not exist.");
        }

        return s;
    }

    /**
     * Updates an existing Student
     *
     * @param student
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @param coops
     * @param notifs
     * @return the updated Student
     */
    @Transactional
    public Student updateStudent(
            Student student,
            String firstName,
            String lastName,
            String email,
            String studentId,
            Set<Coop> coops,
            Set<Notification> notifs,
            Set<Report> reports) {

        StringBuilder error = new StringBuilder();
        if (student == null) {
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
            student.setFirstName(firstName.trim());
        }
        if (lastName != null) {
            student.setLastName(lastName.trim());
        }
        if (email != null) {
            student.setEmail(email.trim());
        }
        if (studentId != null) {
            student.setStudentId(studentId);
        }
        if (notifs != null) {
            student.setNotifications(notifs);
        }
        if (coops != null) {
            student.setCoops(coops);
        }
        if (reports != null) {
            student.setReports(reports);
        }

        return studentRepository.save(student);
    }

    /**
     * Deletes an existing Student
     *
     * @param student
     * @return the deleted Student
     */
    @Transactional
    public Student deleteStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Student to delete cannot be null.");
        }

        studentRepository.delete(student);
        return student;
    }

    /**
     * Deletes an existing Student by student ID
     *
     * @param id
     * @return the deleted Student
     */
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
