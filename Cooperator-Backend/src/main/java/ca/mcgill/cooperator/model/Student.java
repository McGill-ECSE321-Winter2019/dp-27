package ca.mcgill.cooperator.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Entity;

@Entity
public class Student {
    private String firstName;
    private String lastName;
    private String email;
	@Id
	private int id;

    private List<Coop> coops;
    
    @OneToMany(mappedBy = "student")
	public List<Coop> getCoops() {
		return this.coops;
	}

	public void setCoop(List<Coop> coops) {
		this.coops = coops;
	}
	
	private List<Notification> studentReceived;

	@OneToMany(mappedBy = "notifications", fetch = FetchType.EAGER)
	public List<Notification> getStudentReceived() {
		return this.studentReceived;
	}

	public void setStudentReceived(List<Notification> studentReceived) {
		this.studentReceived = studentReceived;
	}


}