package ca.mcgill.cooperator.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;

@Service
public class CourseService {
	@Autowired CourseRepository courseRepository;
	
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
        c.setName(name);
        c.setCourseOfferings(new ArrayList<CourseOffering>());
        
        return courseRepository.save(c);
	}
}
