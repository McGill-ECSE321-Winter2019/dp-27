package ca.mcgill.cooperator.dto;

public class CoopDetailsDto {
    private int id;
    private double payPerHour;
    private int hoursPerWeek;

    private EmployerContactDto employerContact;
    private CoopDto coop;

    public CoopDetailsDto() {}
    
    public CoopDetailsDto(
            int id,
            double payPerHour,
            int hoursPerWeek,
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
