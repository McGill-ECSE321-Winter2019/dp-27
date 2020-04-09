package ca.mcgill.cooperator.model;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CoopDetails {
    @Id @GeneratedValue private int id;
    private int payPerHour;
    private int hoursPerWeek;
    private Date startDate;
    private Date endDate;

    @ManyToOne(optional = false)
    private EmployerContact employerContact;

    @OneToOne private Coop coop;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public int getPayPerHour() {
        return this.payPerHour;
    }

    public void setPayPerHour(int payPerHour) {
        this.payPerHour = payPerHour;
    }

    public int getHoursPerWeek() {
        return this.hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EmployerContact getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContact employerContact) {
        this.employerContact = employerContact;
    }

    public Coop getCoop() {
        return this.coop;
    }

    public void setCoop(Coop coop) {
        this.coop = coop;
    }
}
