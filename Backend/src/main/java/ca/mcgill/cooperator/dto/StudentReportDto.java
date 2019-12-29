package ca.mcgill.cooperator.dto;

import java.util.List;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;

public class StudentReportDto {
	private int id;
    private ReportStatus status;
    
    private Coop coop;
    private List<ReportSection> reportSections;
    
    public StudentReportDto(int id, ReportStatus status, Coop coop, List<ReportSection> reportSections) {
    	this.id = id;
    	this.status = status;
    	this.coop = coop;
    	this.reportSections = reportSections;
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
