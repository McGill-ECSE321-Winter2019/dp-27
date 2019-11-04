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

    @ManyToOne private EmployerContact employerContact;

    @OneToMany private List<ReportSection> reportSections;
    
    /*--- Constructors ---*/
    
    public EmployerReport(Coop coop, EmployerContact employerContact, List<ReportSection> reportSections, ReportStatus reportStatus) {
    	this.coop = coop;
    	this.employerContact = employerContact;
    	this.reportSections = reportSections;
    	this.status = reportStatus;
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
    
    public EmployerContact getEmployerContact() {
    	return this.employerContact;
    }
    
    public void setEmployerContact(EmployerContact employerContact) {
    	this.employerContact = employerContact;
    }
    
    public List<ReportSection> getReportSections() {
    	return this.reportSections;
    }
    
    public void setReportSections(List<ReportSection> reportSections) {
    	this.reportSections = reportSections;
    }

}
