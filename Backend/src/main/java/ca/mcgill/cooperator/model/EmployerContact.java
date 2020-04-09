package ca.mcgill.cooperator.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EmployerContact extends Author {

    private String phoneNumber;

    @ManyToOne(optional = false)
    private Company company;

    @OneToMany(
            mappedBy = "employerContact",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CoopDetails> coopdetails;

    /*--- Getters and Setters ---*/

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<CoopDetails> getCoopDetails() {
        return this.coopdetails;
    }

    public void setCoopDetails(Set<CoopDetails> coopDetails) {
        if (this.coopdetails == null) {
            this.coopdetails = coopDetails;
        } else {
            this.coopdetails.clear();
            this.coopdetails.addAll(coopDetails);
        }
    }
}
