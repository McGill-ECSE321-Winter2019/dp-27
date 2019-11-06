package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EmployerContact {
    @Id @GeneratedValue private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @ManyToOne private Company company;

    @OneToMany(mappedBy = "employerContact")
    private List<CoopDetails> coopdetails;

    @OneToMany(mappedBy = "employerContact")
    private List<EmployerReport> employerReports;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<CoopDetails> getCoopDetails() {
        return this.coopdetails;
    }

    public void setCoopDetails(List<CoopDetails> coopDetails) {
        this.coopdetails = coopDetails;
    }

    public List<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReport> employerReports) {
        this.employerReports = employerReports;
    }
}
