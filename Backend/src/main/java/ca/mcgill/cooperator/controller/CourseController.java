package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.service.CourseOfferingService;
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
    @Autowired private CourseOfferingService courseOfferingService;
    
    /**
     * Creates a new Course
     *
     * <p>In request body:
     *
     * @param name
     * @return the created Course
     */
    @PostMapping("")
    public CourseDto createCourse(@RequestBody CourseDto c) {
        Course createdCourse = courseService.createCourse(c.getName());

        return ControllerUtils.convertToDto(createdCourse);
    }

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
     * Gets all Courses
     *
     * @return List of CourseDto objects
     */
    @GetMapping("")
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        return ControllerUtils.convertCourseListToDto(courses);
    }

    /**
     * Gets all Course names
     *
     * @return List of Course names
     */
    @GetMapping("/names")
    public List<String> getAllCourseNames() {
        return courseService.getAllCourseNames();
    }

    /**
     * Updates an existing Course
     * 
     * @param id
     *
     * <p>In request body:
     *
     * @param name
     * @param courseOfferings
     * @return the updated Course
     */
    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable int id, @RequestBody CourseDto c) {
        Course course = courseService.getCourseById(id);

        List<CourseOffering> courseOfferings = null;
        if (c.getCourseOfferings() != null) {
            courseOfferings =
                    ControllerUtils.convertCourseOfferingListToDomainObject(
                            courseOfferingService, c.getCourseOfferings());
        }

        Course updatedCourse = courseService.updateCourse(course, c.getName(), courseOfferings);

        return ControllerUtils.convertToDto(updatedCourse);
    }

    /**
     * Deletes an existing Course
     *
     * @param id
     * @return the deleted Course
     */
    @DeleteMapping("/{id}")
    public CourseDto deleteCourse(@PathVariable int id) {
        Course course = courseService.getCourseById(id);

        return ControllerUtils.convertToDto(courseService.deleteCourse(course));
    }
}
