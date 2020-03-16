package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class EmployerReportSection {
    @Id @GeneratedValue private int id;
    @Lob private String response;

    @ManyToOne(optional = false)
    private EmployerReport employerReport;

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

    public EmployerReport getEmployerReport() {
        return this.employerReport;
    }

    public void setEmployerReport(EmployerReport employerReport) {
        this.employerReport = employerReport;
    }

    public ReportSectionConfig getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfig reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }
}
