package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.StudentReport;

public interface StudentReportRepository extends CrudRepository<StudentReport, Integer>{

}