package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.EmployerContact;

public class CoopDetailsDto {
    private int id;
    private double payPerHour;
    private int hoursPerWeek;

    private EmployerContact employerContact;
    private Coop coop;

    public CoopDetailsDto(
            int id,
            double payPerHour,
            int hoursPerWeek,
            EmployerContact employerContact,
            Coop coop) {
        this.id = id;
        this.payPerHour = payPerHour;
        this.hoursPerWeek = hoursPerWeek;
        this.employerContact = employerContact;
        this.coop = coop;
    }

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
