package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> {
	
	Course findByName(String name);
}
