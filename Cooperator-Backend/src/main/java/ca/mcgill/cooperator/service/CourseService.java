package ca.mcgill.cooperator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;

@Service
public class CourseService {
	@Autowired CourseRepository courseRepository;
	
	/**
	 * create new course in database
	 * @param name
	 * @return created course
	 */

    @Transactional
    public Course createCourse(String name) {
        StringBuilder error = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            error.append("Course name cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        Course c = new Course();
        c.setName(name.trim());

        c.setCourseOfferings(new ArrayList<CourseOffering>());
        return courseRepository.save(c);
	}
    
    @Transactional
    public Course updateCourse(Course c, String name) {
    	StringBuilder error = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            error.append("Course name cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        c.setName(name.trim());
    	return courseRepository.save(c);
    }
    
    @Transactional
    public Course getCourseByName(String name) {
    	Course c = courseRepository.findByName(name.trim());
    	if(c == null) {
    		 throw new IllegalArgumentException("Course with name " + name + " does not exist!");
    	}
    	return c;
    }
    
    @Transactional
    public Course getCourseById(int id) {
    	Course c = courseRepository.findById(id).orElse(null);
    	if(c == null) {
    		 throw new IllegalArgumentException("Course with ID " + id + " does not exist!");
    	}
    	return c;
    }
    
    @Transactional
    public List<Course> getAllCourses() {
    	return ServiceUtils.toList(courseRepository.findAll());
    }
    
    @Transactional
    public void deleteCourse(Course c) {
    	
    }
}
