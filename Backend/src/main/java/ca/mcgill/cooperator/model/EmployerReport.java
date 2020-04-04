package ca.mcgill.cooperator.model;

import java.util.Set;
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
public class EmployerReport {
    @Id @GeneratedValue private int id;
    private String title;
    private String type;
    private ReportStatus status;

    private byte[] data;

    @ManyToOne private Coop coop;

    @ManyToOne private EmployerContact employerContact;

    @OneToMany(
            mappedBy = "employerReport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<EmployerReportSection> reportSections;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Coop getCoop() {
        return this.coop;
    }

    public void setCoop(Coop coop) {
        this.coop = coop;
    }

    public EmployerContact getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContact employerContact) {
        this.employerContact = employerContact;
    }

    public Set<EmployerReportSection> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(Set<EmployerReportSection> reportSections) {
        if (this.reportSections == null) {
            this.reportSections = reportSections;
        } else {
            this.reportSections.clear();
            this.reportSections.addAll(reportSections);
        }
    }
}
