package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Notification {
    @Id @GeneratedValue private int id;
    private String title;
    private String body;

    @ManyToOne private Student student;

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne(optional = false)
    private Admin sender;

    public Admin getSender() {
        return this.sender;
    }

    public void setSender(Admin sender) {
        this.sender = sender;
    }
}
