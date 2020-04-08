package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportResponseType;
import java.util.List;

public class ReportSectionConfigDto {
    private Integer id;
    private String sectionPrompt;
    private ReportResponseType responseType;
    private Integer questionNumber;

    private ReportConfigDto reportConfig;
    private List<ReportSectionDto> reportSections;

    public ReportSectionConfigDto() {}

    public ReportSectionConfigDto(
            Integer id,
            String sectionPrompt,
            ReportResponseType responseType,
            Integer questionNumber,
            ReportConfigDto reportConfig,
            List<ReportSectionDto> reportSections) {
        this.id = id;
        this.sectionPrompt = sectionPrompt;
        this.responseType = responseType;
        this.questionNumber = questionNumber;
        this.reportConfig = reportConfig;
        this.reportSections = reportSections;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
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

    public Integer getQuestionNumber() {
        return this.questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public ReportConfigDto getReportConfig() {
        return this.reportConfig;
    }

    public void setReportConfig(ReportConfigDto reportConfig) {
        this.reportConfig = reportConfig;
    }

    public List<ReportSectionDto> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSectionDto> reportSections) {
        this.reportSections = reportSections;
    }
}
