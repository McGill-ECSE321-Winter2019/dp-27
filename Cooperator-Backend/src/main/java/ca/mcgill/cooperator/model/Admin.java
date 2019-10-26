package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class Admin {
	@Id
	private int id;
    String firstName;
    String lastName;
    String email;
    
    private List<Notification> sent;

	@OneToMany(mappedBy = "notifications", fetch = FetchType.EAGER)
	public List<Notification> getStudentReceived() {
		return this.sent;
	}

	public void setStudentReceived(List<Notification> sent) {
		this.sent = sent;
	}

}