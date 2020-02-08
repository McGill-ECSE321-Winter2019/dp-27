package ca.mcgill.cooperator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@Autowired CourseService courseService;

    @GetMapping("/{id}")
    public CourseDto getCourseById(@PathVariable int id) {
    	
        return ControllerUtils.convertToDto(courseService.getCourseById(id));
    }
    
    @PostMapping("")
    public CourseDto createCourse(@RequestBody CourseDto courseDto) {
    	Course course = new Course();
    	
    	course = courseService.createCourse(courseDto.getName());
    	
    	return ControllerUtils.convertToDto(course);
    }
}
