package ca.mcgill.cooperator.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Student extends Author {

    @Column(unique = true)
    private String studentId;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Coop> coops;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Notification> studentReceived;

    /*--- Getters and Setters ---*/

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Set<Coop> getCoops() {
        return this.coops;
    }

    public void setCoops(Set<Coop> coops) {
        if (this.coops == null) {
            this.coops = coops;
        } else {
            this.coops.clear();
            this.coops.addAll(coops);
        }
    }

    public Set<Notification> getNotifications() {
        return this.studentReceived;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.studentReceived == null) {
            this.studentReceived = notifications;
        } else {
            this.studentReceived.clear();
            this.studentReceived.addAll(notifications);
        }
    }
}
