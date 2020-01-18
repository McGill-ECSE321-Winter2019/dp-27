package ca.mcgill.cooperator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.service.CourseService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("courses")
public class CourseController {
	
	@Autowired private CourseService courseService;

	/**
     * Get a Course by ID
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
     * Get all courses
     *
     * @return List of CourseDto objects
     */
    @GetMapping("")
    public List<CourseDto> getAllAdmins() {
        List<Course> courses = courseService.getAllCourses();

        return ControllerUtils.convertCourseListToDto(courses);
    }
    
    /**
     * Create a new Course
     *
     * <p>In request body:
     *
     * @param course name
     */
    @PostMapping("")
    public CourseDto createCourse(@RequestBody CourseDto c) {
        Course createdCourse =
                courseService.createCourse(c.getName());

        return ControllerUtils.convertToDto(createdCourse);
    }
    
    /**
    * Update an existing Course
    *
    * <p>In request body:
    *
    * @param name
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
    * Delete an existing Course
    *
    * @param id
    * @return deleted course
    */
   @DeleteMapping("/{id}")
   public CourseDto deleteCourse(@PathVariable int id) {
       Course course = courseService.getCourseById(id);
       Course deletedCourse = courseService.deleteCourse(course);

       return ControllerUtils.convertToDto(deletedCourse);
   }

   @ExceptionHandler(RuntimeException.class)
   public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
       return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
