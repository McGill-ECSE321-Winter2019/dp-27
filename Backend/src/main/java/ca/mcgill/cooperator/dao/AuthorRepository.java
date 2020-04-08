package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Author findByEmail(String email);
}
