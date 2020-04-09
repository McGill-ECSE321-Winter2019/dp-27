package ca.mcgill.cooperator.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Admin extends Author {

    @OneToMany(
            mappedBy = "sender",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Notification> sent;

    /*--- Getters and Setters ---*/

    public Set<Notification> getSentNotifications() {
        return this.sent;
    }

    public void setSentNotifications(Set<Notification> sent) {
        if (this.sent == null) {
            this.sent = sent;
        } else {
            this.sent.clear();
            this.sent.addAll(sent);
        }
    }
}
