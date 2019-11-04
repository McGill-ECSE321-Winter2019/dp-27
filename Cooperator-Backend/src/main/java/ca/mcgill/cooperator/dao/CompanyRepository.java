package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Integer> {}
