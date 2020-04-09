package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, Integer> {}
