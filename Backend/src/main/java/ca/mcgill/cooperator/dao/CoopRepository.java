package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Student;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CoopRepository extends CrudRepository<Coop, Integer> {
    List<Coop> findByStatus(CoopStatus coopStatus);

    List<Coop> findByStudent(Student s);
}
