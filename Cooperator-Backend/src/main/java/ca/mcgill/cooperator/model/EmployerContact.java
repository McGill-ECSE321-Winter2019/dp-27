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

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @OneToMany(mappedBy = "employerContact")
    private List<CoopDetails> coopdetails;

    public List<CoopDetails> getCoopDetails() {
        return this.coopdetails;
    }

    public void setCoop(List<CoopDetails> coopdetails) {
        this.coopdetails = coopdetails;
    }

    @OneToMany(mappedBy = "employerContact")
    private List<EmployerReport> employerReports;

    public List<EmployerReport> getEmployerReports() {
        return this.employerReports;
    }

    public void setEmployerReports(List<EmployerReport> employerReports) {
        this.employerReports = employerReports;
    }
}
