package ca.mcgill.cooperator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;

@Service
public class CourseOfferingService {
	@Autowired CourseOfferingRepository courseOfferingRepository;
	@Autowired CourseRepository courseRepository;
	
	/**
	 * create new course offering in database
	 * @param year
	 * @param season
	 * @param c
	 * @return created course offering
	 */
	@Transactional
	public CourseOffering createCourseOffering(int year, Season season, Course c) {
		StringBuilder error = new StringBuilder();
        if (year <= 0) {
            error.append("Year is invalid! ");
        }
        if (season == null) {
            error.append("Season cannot be null! ");
        }
        if (c == null) {
        	error.append("Course cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        
        CourseOffering co = new CourseOffering();
        co.setYear(year);
        co.setSeason(season);
        co.setCourse(c);
        co.setCoops(new ArrayList<Coop>());
        
        courseOfferingRepository.save(co);
        
        List <CourseOffering> offerings = c.getCourseOfferings();
		offerings.add(co);
		c.setCourseOfferings(offerings);
		
		courseRepository.save(c);
		
		return courseOfferingRepository.save(co);
	}
}
