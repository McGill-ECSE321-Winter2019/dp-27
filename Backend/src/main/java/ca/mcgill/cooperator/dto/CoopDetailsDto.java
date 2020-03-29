package ca.mcgill.cooperator.dto;

import java.sql.Date;

public class CoopDetailsDto {
    private int id;
    private int payPerHour;
    private int hoursPerWeek;
    private Date startDate;
    private Date endDate;

    private EmployerContactDto employerContact;
    private CoopDto coop;

    public CoopDetailsDto() {}

    public CoopDetailsDto(
            int id,
            int payPerHour,
            int hoursPerWeek,
            Date startDate,
            Date endDate,
            EmployerContactDto employerContact,
            CoopDto coop) {
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

    public EmployerContactDto getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContactDto employerContact) {
        this.employerContact = employerContact;
    }

    public CoopDto getCoop() {
        return this.coop;
    }

    public void setCoop(CoopDto coop) {
        this.coop = coop;
    }
}
