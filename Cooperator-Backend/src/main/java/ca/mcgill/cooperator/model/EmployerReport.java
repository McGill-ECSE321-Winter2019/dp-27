package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EmployerReport {
	@Id
	private int id;
    private ReportStatus status;
    
    private Coop coop;
    
    @ManyToOne
    public Coop getCoop() {
    	return this.coop;
    }
    
    public void setCoop(Coop coop) {
    	 this.coop = coop;
    }
    
    private EmployerContact employer;
    
    @ManyToOne
    public EmployerContact getEmployerContact() {
    	return this.employer;
    }
    
    public void setEmployerContact(EmployerContact employer) {
    	 this.employer = employer;
    }
    
    private ReportItem reportItem;
    
    @OneToMany
    public ReportItem getReportItem() {
    	return this.reportItem;
    }
    
    public void setReportItem(ReportItem reportItem) {
    	this.reportItem = reportItem;
    }
}