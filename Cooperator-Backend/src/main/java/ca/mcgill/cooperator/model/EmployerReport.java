package ca.mcgill.cooperator.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class EmployerReport {
	@Id @GeneratedValue
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
    
    private List<ReportSection> reportSections;
    
    public List<ReportSection> getReportSections(){
    	return this.reportSections;
    }

	public void setReportSections(List<ReportSection> reportSections){
		this.reportSections = reportSections;
	}
}