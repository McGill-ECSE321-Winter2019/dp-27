package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.ReportItem;

public interface ReportItemRepository extends CrudRepository<ReportItem, Integer>{

}