package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportResponseType;
import java.util.List;

public class ReportSectionConfigDto {
    private int id;
    private String sectionPrompt;
    private ReportResponseType responseType;

    private ReportConfigDto reportConfig;
    private List<EmployerReportSectionDto> employerReportSections;
    private List<StudentReportSectionDto> studentReportSections;

    public ReportSectionConfigDto() {}

    public ReportSectionConfigDto(
            int id,
            String sectionPrompt,
            ReportResponseType responseType,
            ReportConfigDto reportConfig,
            List<EmployerReportSectionDto> employerReportSections,
            List<StudentReportSectionDto> studentReportSections) {
        this.id = id;
        this.sectionPrompt = sectionPrompt;
        this.responseType = responseType;
        this.reportConfig = reportConfig;
        this.employerReportSections = employerReportSections;
        this.studentReportSections = studentReportSections;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getSectionPrompt() {
        return this.sectionPrompt;
    }

    public void setSectionPrompt(String sectionPrompt) {
        this.sectionPrompt = sectionPrompt;
    }

    public ReportResponseType getResponseType() {
        return this.responseType;
    }

    public void setResponseType(ReportResponseType responseType) {
        this.responseType = responseType;
    }

    public ReportConfigDto getReportConfig() {
        return this.reportConfig;
    }

    public void setReportConfig(ReportConfigDto reportConfig) {
        this.reportConfig = reportConfig;
    }

    public List<EmployerReportSectionDto> getEmployerReportSections() {
        return this.employerReportSections;
    }

    public void setEmployerReportSections(List<EmployerReportSectionDto> employerReportSections) {
        this.employerReportSections = employerReportSections;
    }

    public List<StudentReportSectionDto> getStudentReportSections() {
        return this.studentReportSections;
    }

    public void setStudentReportSections(List<StudentReportSectionDto> studentReportSections) {
        this.studentReportSections = studentReportSections;
    }
}
