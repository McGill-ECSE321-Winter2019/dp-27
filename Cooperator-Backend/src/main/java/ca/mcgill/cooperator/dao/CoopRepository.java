package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Coop;

public interface CoopRepository extends CrudRepository<Coop, Integer>{

}