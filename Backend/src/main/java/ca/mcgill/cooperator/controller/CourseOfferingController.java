package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
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
@RequestMapping("course-offerings")
public class CourseOfferingController {

    @Autowired CourseOfferingService courseOfferingService;
    @Autowired CourseService courseService;

    @GetMapping("/{id}")
    public CourseOfferingDto getCourseOfferingById(@PathVariable int id) {
        return ControllerUtils.convertToDto(courseOfferingService.getCourseOfferingById(id));
    }
    
    @GetMapping("")
    public List<CourseOfferingDto> getCourseOfferings() {
        return ControllerUtils.convertCourseOfferingListToDto(courseOfferingService.getAllCourseOfferings());
    }
    
    @GetMapping("/by-course/{id}")
    public List<CourseOfferingDto> getCourseOfferingsByCourse(@PathVariable int id) {
    	Course course = courseService.getCourseById(id);
    	List<CourseOffering> cos = courseOfferingService.getCourseOfferingsByCourse(course);
    	return ControllerUtils.convertCourseOfferingListToDto(cos);
    }

    @PostMapping("")
    public CourseOfferingDto createCourseOfferingDto(
            @RequestBody CourseOfferingDto courseOfferingDto) {
        CourseDto courseDto = courseOfferingDto.getCourse();
        Course course = courseService.getCourseById(courseDto.getId());

        CourseOffering courseOffering =
                courseOfferingService.createCourseOffering(
                        courseOfferingDto.getYear(), courseOfferingDto.getSeason(), course);
        return ControllerUtils.convertToDto(courseOffering);
    }
    
    @PutMapping("")
    public CourseOfferingDto updateCourseOfferingDto(
            @RequestBody CourseOfferingDto coDto) {
    	CourseOffering co = courseOfferingService.getCourseOfferingById(coDto.getId());
        Course course = courseService.getCourseById(coDto.getCourse().getId());

        CourseOffering courseOffering =
                courseOfferingService.updateCourseOffering(
                		co, coDto.getYear(), coDto.getSeason(), course);
        return ControllerUtils.convertToDto(courseOffering);
    }
    
    @DeleteMapping("/{id}")
    public CourseOfferingDto deleteCourseOfferingDto(@PathVariable int id){
    	CourseOffering co = courseOfferingService.getCourseOfferingById(id);
    	return ControllerUtils.convertToDto(courseOfferingService.deleteCourseOffering(co));
    }
    
}
