package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class Admin {
	@Id @GeneratedValue
	private int id;
    String firstName;
    String lastName;
    String email;
    
    private List<Notification> sent;

	@OneToMany(mappedBy = "notifications", fetch = FetchType.EAGER)
	public List<Notification> getNotificationsSent() {
		return this.sent;
	}

	public void setNotificationsSent(List<Notification> sent) {
		this.sent = sent;
	}

}