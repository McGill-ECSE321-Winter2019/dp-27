package ca.mcgill.cooperator.dto;

import java.util.List;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Notification;

public class StudentDto {
	private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentId;
    
    private List<Coop> coops;
    private List<Notification> studentReceived;
    
    public StudentDto(int id, String firstName, String lastName, String email, String studentId, List<Coop> coops, List<Notification> studentReceived) {
    	this.id = id;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.email = email;
    	this.studentId = studentId;
    	this.coops = coops;
    	this.studentReceived = studentReceived;
    }
    
    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Coop> getCoops() {
        return this.coops;
    }

    public void setCoops(List<Coop> coops) {
        this.coops = coops;
    }

    public List<Notification> getNotifications() {
        return this.studentReceived;
    }

    public void setNotifications(List<Notification> notifications) {
        this.studentReceived = notifications;
    }
}
