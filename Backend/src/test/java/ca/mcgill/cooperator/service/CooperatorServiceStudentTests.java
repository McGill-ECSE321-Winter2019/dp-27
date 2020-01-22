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

import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Student;

@SpringBootTest
public class CooperatorServiceStudentTests {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    public void testCreateStudent() {
    	String firstName = "Albert";
    	String lastName = "Kragl";
    	String email = "frisbeeGod47@gmail.com";
    	String studentID = "260735111";
    	
    	try {
        studentService.createStudent(firstName, lastName, email, studentID);
    	} catch(IllegalArgumentException e) {
    		fail();
    	}
    	
    	Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(),firstName);
        assertEquals(s.getLastName(),lastName);
        assertEquals(s.getEmail(),email);
        assertEquals(s.getStudentId(),studentID);
    }

    @Test
    public void testCreateStudentWithNotifications(){
        String firstName = "Albert";
    	String lastName = "Kragl";
    	String email = "frisbeeGod47@gmail.com";
        String studentID = "260735111";
        List<Coop> coops = new ArrayList<>();
    	List<Notification> notifications = new ArrayList<>();
    	
    	try {
        studentService.createStudent(firstName, lastName, email, studentID, coops, notifications);
    	} catch(IllegalArgumentException e) {
    		fail();
    	}
    	
    	Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(),firstName);
        assertEquals(s.getLastName(),lastName);
        assertEquals(s.getEmail(),email);
        assertEquals(s.getStudentId(),studentID);
    }

    @Test
    public void testCreateStudentNull() {
        try{
            studentService.createStudent(null, null, null, null);
            fail();
        } catch(IllegalArgumentException e){
            assertEquals("FirstName is null or invalid. LastName is null or invalid. Email is null or invalid. StudentID is null or invalid.", e.getMessage().trim());
        }
    }

    @Test
    public void testCreateStudentEmpty() {
        try{
            studentService.createStudent("","","","");
            fail();
        } catch(IllegalArgumentException e){
            assertEquals("FirstName is null or invalid. LastName is null or invalid. Email is null or invalid. StudentID is null or invalid.", e.getMessage().trim());
        }
    }

    @Test
    public void testCreateStudentSpaces() {
        try{
            studentService.createStudent(" "," "," "," ");
            fail();
        } catch(IllegalArgumentException e){
            assertEquals("FirstName is null or invalid. LastName is null or invalid. Email is null or invalid. StudentID is null or invalid.", e.getMessage().trim());
        }
    }

    @Test
    public void testUpdateStudent() {
        String firstName = "Kah";
        String lastName = "Shew";
        String email = "kah.shew@nuts.com";
        String studentID = "260727105";

    	try {
           studentService.createStudent(firstName, lastName, email, studentID);
        } catch(IllegalArgumentException e) {
            fail();
        }
    	firstName = "P";
    	lastName = "Nut";
    	email = "p.nut@legume.com";
    	String studentIDNew = "260745100";
    	List<Coop> coops = new ArrayList<>();
    	List<Notification> notifications = new ArrayList<>();
    	
        try{
            Student s = studentService.getStudentByStudentID(studentID);
            studentService.updateStudent(s, firstName, lastName, email, studentIDNew, coops, notifications);
        }catch(IllegalArgumentException e) {
        	fail();
        }
        Student s = studentService.getStudentByStudentID(studentIDNew);
        assertEquals(s.getFirstName(), firstName);
        assertEquals(s.getLastName(), lastName);
        assertEquals(s.getEmail(), email);
        assertEquals(s.getStudentId(), studentIDNew);
    }

    @Test
    public void testUpdateStudentInvalid() {
    	 String firstName = "Kah";
         String lastName = "Shew";
         String email = "kah.shew@nuts.com";
         String studentID = "260727105";

     	try {
            studentService.createStudent(firstName, lastName, email, studentID);
         } catch(IllegalArgumentException e) {
             fail();
         }
     	
         try{
             Student s = studentService.getStudentByStudentID(studentID);
             studentService.updateStudent(s, "  ", "", "", "", null, null);
         }catch(IllegalArgumentException e) {
             fail();
         }
         Student s = studentService.getStudentByStudentID(studentID);
         assertEquals(s.getFirstName(), firstName);
         assertEquals(s.getLastName(), lastName);
         assertEquals(s.getEmail(), email);
         assertEquals(s.getStudentId(), studentID);
         
    }

    @Test
    public void testDeleteStudent() {
        String firstName = "Kah";
        String lastName = "Shew";
        String email = "kah.shew@nuts.com";
        String studentID = "260727105";

    	try {
           studentService.createStudent(firstName, lastName, email, studentID);
        } catch(IllegalArgumentException e) {
            fail();
        }
    	
    	Student s = studentService.getStudentByStudentID(studentID);
        assertEquals(s.getFirstName(),firstName);
        assertEquals(s.getLastName(),lastName);
        assertEquals(s.getEmail(),email);
        assertEquals(s.getStudentId(),studentID);
        
    	try {
    		studentService.deleteStudentByStudentID(studentID);
    	}catch(IllegalArgumentException e) {
    		fail();
    	}
    }
}