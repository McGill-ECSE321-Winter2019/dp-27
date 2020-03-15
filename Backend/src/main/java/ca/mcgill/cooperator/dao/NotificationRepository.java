package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    
    List<Notification> findByStudent(Student student);
    
}
