package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Admin;

public interface AdminRepository extends CrudRepository<Admin, Integer>{

}