package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StudentReportSection {
    @Id @GeneratedValue private int id;
    private String response;

    @ManyToOne(optional = false)
    private StudentReport studentReport;

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

    public StudentReport getStudentReport() {
        return this.studentReport;
    }

    public void setStudentReport(StudentReport studentReport) {
        this.studentReport = studentReport;
    }

    public ReportSectionConfig getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfig reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }
}
