package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Report;

public interface ReportRepository extends CrudRepository<Report, Integer> {}
