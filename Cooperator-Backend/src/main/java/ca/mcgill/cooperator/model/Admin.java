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
    String firstName;
    String lastName;
    String email;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private List<Notification> sent;

    public List<Notification> getNotificationsSent() {
        return this.sent;
    }

    public void setNotificationsSent(List<Notification> sent) {
        this.sent = sent;
    }
}
