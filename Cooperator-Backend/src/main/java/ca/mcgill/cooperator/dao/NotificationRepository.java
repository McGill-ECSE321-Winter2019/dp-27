package ca.mcgill.cooperator.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.cooperator.model.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Integer>{

}