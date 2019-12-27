package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    Admin findByEmail(String email);
}
