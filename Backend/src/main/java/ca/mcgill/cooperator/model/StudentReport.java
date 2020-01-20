package ca.mcgill.cooperator.model;

import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportSection> reportSections;

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
    	if (this.reportSections == null) {
      		this.reportSections = reportSections;
       	} else {
           	this.reportSections.clear();
            this.reportSections.addAll(reportSections);
        }
    }
}
