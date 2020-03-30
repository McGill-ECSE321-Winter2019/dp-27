package ca.mcgill.cooperator.dto;

public class CoopDetailsDto {
    private Integer id;
    private Integer payPerHour;
    private Integer hoursPerWeek;

    private EmployerContactDto employerContact;
    private CoopDto coop;

    public CoopDetailsDto() {}

    public CoopDetailsDto(
    		Integer id,
    		Integer payPerHour,
    		Integer hoursPerWeek,
            EmployerContactDto employerContact,
            CoopDto coop) {
        this.id = id;
        this.payPerHour = payPerHour;
        this.hoursPerWeek = hoursPerWeek;
        this.employerContact = employerContact;
        this.coop = coop;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public Integer getPayPerHour() {
        return this.payPerHour;
    }

    public void setPayPerHour(Integer payPerHour) {
        this.payPerHour = payPerHour;
    }

    public Integer getHoursPerWeek() {
        return this.hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
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
