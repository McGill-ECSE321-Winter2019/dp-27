package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Admin {
    @Id @GeneratedValue private int id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private List<Notification> sent;
    
    /*--- Constructors ---*/
    
    public Admin(String firstName, String lastName, String email, List<Notification> sent) { 
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.email = email;
    	this.sent = sent;
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
    
    public List<Notification> getSentNotifications() {
    	return this.sent;
    }
    
    public void setSentNotifications(List<Notification> sent) {
    	this.sent = sent;
    }
}
