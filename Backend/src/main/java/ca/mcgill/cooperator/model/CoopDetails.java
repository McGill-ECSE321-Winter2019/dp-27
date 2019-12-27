package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CoopDetails {
    @Id @GeneratedValue private int id;
    private double payPerHour;
    private int hoursPerWeek;

    @ManyToOne(optional = false)
    private EmployerContact employerContact;

    @OneToOne 
    private Coop coop;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public double getPayPerHour() {
        return this.payPerHour;
    }

    public void setPayPerHour(double payPerHour) {
        this.payPerHour = payPerHour;
    }

    public int getHoursPerWeek() {
        return this.hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
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
