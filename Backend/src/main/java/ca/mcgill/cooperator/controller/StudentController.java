package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.ArrayList;
import java.util.Collection;
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
public class StudentController extends BaseController {

    @Autowired private StudentService studentService;
    @Autowired private CoopService coopService;
    @Autowired private CourseOfferingService courseOfferingService;
    @Autowired private CourseService courseService;
    @Autowired private NotificationService notificationService;

    /**
     * Get students with filters
     *
     * @return Set<StudentDto>
     */
    @GetMapping("")
    public Collection<StudentDto> getStudentFiltered(
            @RequestParam(required = false) Season season,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CoopStatus status) {
        // if all the request params are empty, return all students
        if (year == null
                && season == null
                && (name == null || name.trim().length() == 0)
                && status == null) {
            List<Student> s = studentService.getAllStudents();
            return ControllerUtils.convertStudentListToDto(s);
        }

        // find all the course offerings that correspond to the filters and to set intersections
        Set<CourseOffering> result = courseOfferingService.getAllCourseOfferingsSet();
        if (year != null) {
            Set<CourseOffering> yearCO = courseOfferingService.getCourseOfferings(year);
            result.retainAll(yearCO);
        }
        if (season != null) {

            Set<CourseOffering> seasonCO = courseOfferingService.getCourseOfferings(season);
            result.retainAll(seasonCO);
        }
        if (name != null && name.trim().length() != 0) {
            Set<CourseOffering> nameCO =
                    courseOfferingService.getCourseOfferingsByCourse(
                            courseService.getCourseByName(name));
            result.retainAll(nameCO);
        }

        // find the students that correspond to those course offerings and check the coop status
        // filter
        Set<Student> toReturn = new HashSet<>();
        if (result.size() > 0) {
            result.stream()
                    .forEach(
                            co ->
                                    co.getCoops().stream()
                                            .forEach(
                                                    c -> {
                                                        if (status == null
                                                                || (status != null
                                                                        && c.getStatus()
                                                                                == status)) {
                                                            toReturn.add(c.getStudent());
                                                        }
                                                    }));
        }

        return ControllerUtils.convertStudentSetToDto(toReturn);
    }

    /**
     * Get Student by id
     *
     * @param id
     * @return StudentDto Object
     */
    @GetMapping("id/{id}")
    public StudentDto getStudentById(@PathVariable int id) {
        Student s = studentService.getStudentById(id);

        return ControllerUtils.convertToDto(s);
    }
    /*(
     * Get Student by email
     *
     * @param email
     * @return StudentDto Object
     */
    @GetMapping("email/{email}")
    public StudentDto getStudentByEmail(@PathVariable String email) {
        Student s = studentService.getStudentByEmail(email);

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
    public StudentDto createStudent(@RequestBody StudentDto studentDto) {

        Student student =
                studentService.createStudent(
                        studentDto.getFirstName(),
                        studentDto.getLastName(),
                        studentDto.getEmail(),
                        studentDto.getStudentId());

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
    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable int id, @RequestBody StudentDto s) {
        Student student = studentService.getStudentById(id);

        Set<Coop> coops = null;
        if (s.getCoops() != null) {
            coops = ControllerUtils.convertCoopsListToDomainObject(coopService, s.getCoops());
        }

        Set<Notification> notifs = null;
        if (s.getNotifications() != null) {
            notifs =
                    ControllerUtils.convertNotificationListToDomainObjectSet(
                            notificationService, s.getNotifications());
        }

        studentService.updateStudent(
                student,
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getStudentId(),
                coops,
                notifs);
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
     * Gets current coop for the specified Student
     *
     * @param id
     * @return Coop or null if not in a Coop currently
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
     * Gets the upcoming Coop(s) for the specified Student
     *
     * @param id
     * @return Coop(s) or empty list if no upcoming Coops
     */
    @GetMapping("/{id}/upcoming-coops")
    public List<CoopDto> getUpcomingStudentCoops(@PathVariable int id) {
        Student s = studentService.getStudentById(id);
        Set<Coop> coops = s.getCoops();
        List<CoopDto> result = new ArrayList<>();
        for (Coop c : coops) {
            if (c.getStatus() == CoopStatus.FUTURE
                    || c.getStatus() == CoopStatus.UNDER_REVIEW
                    || c.getStatus() == CoopStatus.INCOMPLETE)
                result.add(ControllerUtils.convertToDto(c));
        }
        return result;
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
