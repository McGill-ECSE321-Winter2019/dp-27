package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    Notification findByTitle(String title);
}
