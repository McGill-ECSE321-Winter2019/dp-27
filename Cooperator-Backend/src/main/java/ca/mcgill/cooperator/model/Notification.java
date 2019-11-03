package ca.mcgill.cooperator.model;

import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Notification {
	@Id
	@GeneratedValue
	private int id;
	private String title;
	private String body;

	private Student student;

	@ManyToOne
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	private Admin sender;

	@ManyToOne(optional = false)
	public Admin getSender() {
		return this.sender;
	}

	public void setSender(Admin sender) {
		this.sender = sender;
	}

}