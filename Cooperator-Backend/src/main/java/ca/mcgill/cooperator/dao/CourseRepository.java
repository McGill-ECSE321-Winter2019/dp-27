package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Course;

public interface CourseRepository extends CrudRepository<Course, Integer>{

}