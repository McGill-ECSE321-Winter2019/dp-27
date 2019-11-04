package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EmployerReport {
    @Id @GeneratedValue private int id;
    private ReportStatus status;

    @ManyToOne private Coop coop;

    public Coop getCoop() {
        return this.coop;
    }

    public void setCoop(Coop coop) {
        this.coop = coop;
    }

    @ManyToOne private EmployerContact employerContact;

    public EmployerContact getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContact employer) {
        this.employerContact = employer;
    }

    @OneToMany private List<ReportSection> reportSections;

    public List<ReportSection> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSection> reportSections) {
        this.reportSections = reportSections;
    }
}
