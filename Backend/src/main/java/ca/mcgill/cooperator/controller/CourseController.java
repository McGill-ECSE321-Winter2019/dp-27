package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("courses")
public class CourseController extends BaseController {

    @Autowired private CourseService courseService;

    /**
     * Gets a Course by ID
     *
     * @param id
     * @return CourseDto object
     */
    @GetMapping("/{id}")
    public CourseDto getCourseById(@PathVariable int id) {
        Course c = courseService.getCourseById(id);
        return ControllerUtils.convertToDto(c);
    }

    /**
     * Gets all courses
     *
     * @return List of CourseDto objects
     */
    @GetMapping("")
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        return ControllerUtils.convertCourseListToDto(courses);
    }

    /**
     * Creates a new Course
     *
     * <p>In request body:
     *
     * @param CourseDto object
     * @return CourseDto object
     */
    @PostMapping("")
    public CourseDto createCourse(@RequestBody CourseDto c) {
        Course createdCourse = courseService.createCourse(c.getName());

        return ControllerUtils.convertToDto(createdCourse);
    }

    /**
     * Updates an existing Course
     *
     * <p>In request body:
     *
     * @param CourseDto object
     * @return CourseDto object
     */
    @PutMapping("")
    public CourseDto updateCourse(@RequestBody CourseDto c) {
        Course course = courseService.getCourseById(c.getId());
        Course updatedCourse =
                courseService.updateCourse(
                        course,
                        c.getName(),
                        ControllerUtils.convertCourseOfferingListToDomainObject(
                                c.getCourseOfferings()));

        return ControllerUtils.convertToDto(updatedCourse);
    }

    /**
     * Deletes an existing Course
     *
     * @param id
     * @return deleted CourseDto object
     */
    @DeleteMapping("/{id}")
    public CourseDto deleteCourse(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        Course deletedCourse = courseService.deleteCourse(course);

        return ControllerUtils.convertToDto(deletedCourse);
    }
}
