package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ReportSection {
	@Id @GeneratedValue private int id;
    private String response;

    @ManyToOne(optional = false)
    private Report report;

    @ManyToOne(optional = false)
    private ReportSectionConfig reportSectionConfig;
    
    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ReportSectionConfig getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfig reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }

}
