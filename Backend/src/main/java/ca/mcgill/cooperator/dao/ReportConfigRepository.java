package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.ReportConfig;
import org.springframework.data.repository.CrudRepository;

public interface ReportConfigRepository extends CrudRepository<ReportConfig, Integer> {

    ReportConfig findByType(String type);
}
