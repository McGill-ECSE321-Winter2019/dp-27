package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.StudentService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired private StudentService studentService;
    /**
     * Get all students
     *
     * @return List<StudentDto>
     */
    @GetMapping("")
    public List<StudentDto> getAllStudents() {
        List<Student> s = studentService.getAllStudents();

        return ControllerUtils.convertToDto(s);
    }
    /**
     * Get Student by id
     *
     * @param id
     * @return StudentDto Object
     */
    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable int id) {
        Student s = studentService.getStudentById(id);

        return ControllerUtils.convertToDto(s);
    }
    /**
     * Create new Student
     *
     * <p>In request body:
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @return created Student
     */
    @PostMapping("")
    public StudentDto createStudent(@RequestBody StudentDto s) {
        Student student =
                studentService.createStudent(
                        s.getFirstName(), s.getLastName(), s.getEmail(), s.getStudentId());

        return ControllerUtils.convertToDto(student);
    }

    /**
     * Update student
     *
     * <p>In request body:
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param studentId
     * @param List<Coops>
     * @param List<Notifications>
     * @return updated Student
     */
    @PutMapping("")
    public StudentDto updateStudent(@RequestBody StudentDto s) {
        Student student = studentService.getStudentById(s.getId());
        studentService.updateStudent(
                student,
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getStudentId(),
                ControllerUtils.convertCoopsListToDomainObject(s.getCoops()),
                ControllerUtils.convertNotificationListToDomainObjectSet(s.getNotifications()));
        return ControllerUtils.convertToDto(student);
    }
    /**
     * Deletes an existing student
     *
     * @param id
     * @return deleted Student
     */
    @DeleteMapping("/{id}")
    public StudentDto deleteStudent(@PathVariable int id) {
        Student student = studentService.deleteStudent(studentService.getStudentById(id));
        return ControllerUtils.convertToDto(student);
    }

    /**
     * Gets current coop
     *
     * @param id
     * @return coop or null if not in a coop currently
     */
    @GetMapping("/{id}/current-coop")
    public CoopDto getCurrentStudentCoop(@PathVariable int id) {
        Student s = studentService.getStudentById(id);
        Set<Coop> coops = s.getCoops();
        for (Coop c : coops) {
            if (c.getStatus() == CoopStatus.IN_PROGRESS) return ControllerUtils.convertToDto(c);
        }
        return null;
    }

    /**
     * Get student coops by status
     *
     * @param id
     *     <p>In request body
     * @param status
     * @return set of all coops with that status
     */
    @GetMapping("/{id}/coop-list")
    public Set<CoopDto> getCoopsByStatus(@PathVariable int id, @RequestParam CoopStatus status) {
        Student s = studentService.getStudentById(id);
        Set<Coop> coops = s.getCoops();
        Set<CoopDto> coopDto = new HashSet<>();
        for (Coop c : coops) {
            if (c.getStatus() == status) coopDto.add(ControllerUtils.convertToDto(c));
        }
        return coopDto;
    }
}
