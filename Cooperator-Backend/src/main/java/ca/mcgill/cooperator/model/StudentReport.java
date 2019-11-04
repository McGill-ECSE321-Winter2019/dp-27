package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class StudentReport {
    @Id @GeneratedValue private int id;
    private ReportStatus status;

    @ManyToOne private Coop coop;

    @OneToMany private List<ReportSection> reportSections;

    /*--- Constructors ---*/

    public StudentReport(Coop coop, List<ReportSection> reportSections, ReportStatus status) {
        this.coop = coop;
        this.reportSections = reportSections;
        this.status = status;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Coop getCoop() {
        return this.coop;
    }

    public void setCoop(Coop coop) {
        this.coop = coop;
    }

    public List<ReportSection> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSection> reportSections) {
        this.reportSections = reportSections;
    }
}
