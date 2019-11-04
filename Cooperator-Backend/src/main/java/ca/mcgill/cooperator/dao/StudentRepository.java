package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Student;

public interface StudentRepository extends CrudRepository<Student, Integer>{

}