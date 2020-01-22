package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Student {
    @Id @GeneratedValue private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentId;

    @OneToMany(mappedBy = "student")
    private List<Coop> coops;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Notification> studentReceived;

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
        if (this.coops == null) {
        	this.coops = coops;
        } else {
        	this.coops.clear();
        	this.coops.addAll(coops);
        }
    }

    public List<Notification> getNotifications() {
        return this.studentReceived;
    }

    public void setNotifications(List<Notification> notifications) {
        if (this.studentReceived == null) {
        	this.studentReceived = notifications;
        } else {
        	this.studentReceived.clear();
        	this.studentReceived.addAll(notifications);
        }
        this.studentReceived = notifications;
    }
}
