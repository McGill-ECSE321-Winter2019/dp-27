package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Student;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Integer> {

    List<Student> findByFirstName(String firstName);

    List<Student> findByLastName(String lastName);

    List<Student> findByFirstNameAndLastName(String firstName, String lastName);

    Student findByStudentId(String studentId);

    Student findByEmail(String email);
}
