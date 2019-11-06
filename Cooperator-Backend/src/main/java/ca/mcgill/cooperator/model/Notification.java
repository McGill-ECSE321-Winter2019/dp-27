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

    @ManyToOne(optional = false)
    private Admin sender;

    /*--- Setters and Getters ---*/

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
