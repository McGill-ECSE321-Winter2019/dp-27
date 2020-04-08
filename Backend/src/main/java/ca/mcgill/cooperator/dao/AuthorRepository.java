package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
	Author findByEmail(String email);
}
