package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Company;

public interface CompanyRepository extends CrudRepository<Company, Integer>{

}