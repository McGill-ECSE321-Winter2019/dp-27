package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Student;

public class NotificationDto {
	private int id;
    private String title;
    private String body;
    
    private Student student;
    private Admin sender;
    
    public NotificationDto(int id, String title, String body, Student student, Admin sender) {
    	this.id = id;
    	this.title = title;
    	this.body = body;
    	this.student = student;
    	this.sender = sender;
    }
    
    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Admin getSender() {
        return this.sender;
    }

    public void setSender(Admin sender) {
        this.sender = sender;
    }
}
