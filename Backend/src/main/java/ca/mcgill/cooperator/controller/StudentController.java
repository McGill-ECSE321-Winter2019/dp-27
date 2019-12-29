package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Student;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("students")
public class StudentController {

    @GetMapping("/{id}")
    public String getStudentById(@PathVariable int id) {
        return "Hello World";
    }

    private StudentDto convertToDto(Student s) {
        if (s == null) {
            throw new IllegalArgumentException("Student does not exist!");
        }
        return new StudentDto(
                s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getStudentId(),
                s.getCoops(),
                s.getNotifications());
    }
}
