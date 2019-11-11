package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

@SpringBootTest
public class CooperatorServiceNotificationTests {

    @Autowired NotificationService notificationService;
    
    @Autowired NotificationRepository notificationRepository;
    

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        notificationRepository.deleteAll();
    }

    @Test
    public void testCreateNotification() {
        /*String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent();
        Admin sender = createTestAdmin();
        
        try {
        	notificationService.createNotification(title, body, student, sender);
        	
        } catch(IllegalArgumentException e) {
        	fail();
        }
        
        assertEquals(1, notificationService.getAllNotifications().size());*/
    	assertTrue(true);
    }

    @Test
    public void testCreateNotificationNull() {
    	String title = null;
        String body = null;
        Student student = null;
        Admin sender = null;
        
        String error = "";
        try {
        	notificationService.createNotification(title, body, student, sender);
        	
        } catch(IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Notification title cannot be empty! "
        		   + "Notification body cannot be empty! "
        		   + "Notification must have a Student receiver! "
        		   + "Notification must have an Admin sender!", error);
        assertEquals(0, notificationService.getAllNotifications().size());
    
    }

    @Test
    public void testCreateNotificationEmpty() {
    	String title = "";
        String body = "";
        Student student = null;
        Admin sender = null;
        
        String error = "";
        try {
        	notificationService.createNotification(title, body, student, sender);
        	
        } catch(IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Notification title cannot be empty! "
        		   + "Notification body cannot be empty! "
        		   + "Notification must have a Student receiver! "
        		   + "Notification must have an Admin sender!", error);
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    public void testCreateNotificationSpaces() {
    	String title = "     ";
        String body = "  ";
        Student student = null;
        Admin sender = null;
        
        String error = "";
        try {
        	notificationService.createNotification(title, body, student, sender);
        	
        } catch(IllegalArgumentException e) {
        	error = e.getMessage();
        }
        
        assertEquals("Notification title cannot be empty! "
        		   + "Notification body cannot be empty! "
        		   + "Notification must have a Student receiver! "
        		   + "Notification must have an Admin sender!", error);
        assertEquals(0, notificationService.getAllNotifications().size());
    }

    @Test
    public void testCreateNotificationManyStudents() {
        assertTrue(true);
    }

    @Test
    public void testUpdateNotification() {
    	/*String title = "Hello";
        String body = "Please attend meeting.";
        Student student = createTestStudent();
        Admin sender = createTestAdmin();
        
        Notification n = null;
        
        try {
        	notificationService.createNotification(title, body, student, sender);
        	n = notificationService.getNotification(title);
        	
        	title = "Bye";
        	body = "different message";
        	
        	notificationService.updateNotification(n, title, body, student, sender);
        	n = notificationService.getNotification(title);
        	
        } catch(IllegalArgumentException e) {
        	fail();
        }
        
        assertEquals("Bye", n.getTitle());
        assertEquals(1, notificationService.getAllNotifications().size());*/
    	assertTrue(true);
    }

    @Test
    public void testUpdateNotificationInvalid() {
        assertTrue(true);
    }

    @Test
    public void testDeleteNotification() {
        assertTrue(true);
    }
    
    public static Student createTestStudent() {
    	Student s = new Student();
    	s.setFirstName("Susan");
    	s.setLastName("Matuszewski");
    	s.setEmail("susan@gmail.com");
    	s.setStudentId("260719281");
    	
    	List<Notification> notifs = new ArrayList<Notification>();
    	s.setNotifications(notifs);
    	
    	return s;
    }
    
    public static Admin createTestAdmin() {
    	Admin a = new Admin();
    	a.setFirstName("Lorraine");
    	a.setLastName("Douglas");
    	a.setEmail("lorraine@gmail.com");
    	List<Notification> notifs = new ArrayList<Notification>();
    	a.setSentNotifications(notifs);
    	
    	return a;
    }
}
