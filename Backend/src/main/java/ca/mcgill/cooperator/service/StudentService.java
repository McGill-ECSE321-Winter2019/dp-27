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
public class StudentService {
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
            error.append("Student first name cannot be empty! ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Student last name cannot be empty! ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Student email cannot be empty! ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Student email must be a valid email! ");
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
        s.setStudentId(studentId);
        s.setNotifications(new HashSet<Notification>());
        s.setCoops(new HashSet<Coop>());

        return studentRepository.save(s);
    }

    @Transactional
    public Student getStudentById(int id) {
        Student s = studentRepository.findById(id).orElse(null);
        if (s == null) {
            throw new IllegalArgumentException("Student with ID " + id + " does not exist!");
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
            error.append("Student to update cannot be null! ");
        }
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
        if (coops == null) {
            error.append("Co-ops cannot be null! ");
        }
        if (notifs == null) {
            error.append("Notifs cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        s.setFirstName(firstName.trim());
        s.setLastName(lastName.trim());
        s.setEmail(email.trim());
        s.setStudentId(studentId);
        s.setNotifications(notifs);
        s.setCoops(coops);

        studentRepository.save(s);

        for (Notification notif : notifs) {
            notif.setStudent(s);
            notificationRepository.save(notif);
        }

        for (Coop coop : coops) {
            coop.setStudent(s);
            coopRepository.save(coop);
        }

        return studentRepository.save(s);
    }

    @Transactional
    public Student deleteStudent(Student s) {
        if (s == null) {
            throw new IllegalArgumentException("Student to delete cannot be null!");
        }

        studentRepository.delete(s);
        return s;
    }
}
