package ca.mcgill.cooperator.model;

<<<<<<< HEAD
import java.util.Set;
=======
import java.util.List;
import java.util.Set;

>>>>>>> changing lists to sets for some classes and fixed put request for employer contact
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class EmployerContact {
    @Id @GeneratedValue private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @ManyToOne(optional = false)
    private Company company;

<<<<<<< HEAD
    @OneToMany(
            mappedBy = "employerContact",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<CoopDetails> coopdetails;

    @OneToMany(
            mappedBy = "employerContact",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
=======
    @OneToMany(mappedBy = "employerContact",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.EAGER)
    private Set<CoopDetails> coopdetails;

    @OneToMany(mappedBy = "employerContact", 
    		   cascade = CascadeType.ALL,
    		   orphanRemoval = true,
    		   fetch = FetchType.EAGER)
>>>>>>> changing lists to sets for some classes and fixed put request for employer contact
    private Set<EmployerReport> employerReports;

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
<<<<<<< HEAD
        if (this.coopdetails == null) {
            this.coopdetails = coopDetails;
        } else {
            this.coopdetails.clear();
=======
    	if (this.coopdetails == null) {
      		this.coopdetails = coopDetails;
       	} else {
           	this.coopdetails.clear();
>>>>>>> changing lists to sets for some classes and fixed put request for employer contact
            this.coopdetails.addAll(coopDetails);
        }
    }

    public Set<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(Set<EmployerReport> employerReports) {
<<<<<<< HEAD
        if (this.employerReports == null) {
            this.employerReports = employerReports;
        } else {
            this.employerReports.clear();
=======
    	if (this.employerReports == null) {
      		this.employerReports = employerReports;
       	} else {
           	this.employerReports.clear();
>>>>>>> changing lists to sets for some classes and fixed put request for employer contact
            this.employerReports.addAll(employerReports);
        }
    }
}
