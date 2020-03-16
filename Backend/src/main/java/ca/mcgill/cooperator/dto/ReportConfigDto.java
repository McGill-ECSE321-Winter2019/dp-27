package ca.mcgill.cooperator.dto;

import java.util.List;

public class ReportConfigDto {
    private Integer id;
    private Boolean requiresFile;
    private Integer deadline;
    private Boolean isDeadlineFromStart;
    private String type;

    private List<ReportSectionConfigDto> reportSectionConfigs;

    public ReportConfigDto() {}

    public ReportConfigDto(
            Integer id,
            Boolean requiresFile,
            Integer deadline,
            Boolean isDeadlineFromStart,
            String type,
            List<ReportSectionConfigDto> reportSectionConfigs) {
        this.id = id;
        this.requiresFile = requiresFile;
        this.deadline = deadline;
        this.isDeadlineFromStart = isDeadlineFromStart;
        this.type = type;
        this.reportSectionConfigs = reportSectionConfigs;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public Boolean getRequiresFile() {
        return this.requiresFile;
    }

    public void setRequiresFile(boolean requiresFile) {
        this.requiresFile = requiresFile;
    }

    public Integer getDeadline() {
        return this.deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public Boolean getIsDeadlineFromStart() {
        return this.isDeadlineFromStart;
    }

    public void setIsDeadlineFromStart(boolean isDeadlineFromStart) {
        this.isDeadlineFromStart = isDeadlineFromStart;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ReportSectionConfigDto> getReportSectionConfigs() {
        return this.reportSectionConfigs;
    }

    public void setReportSectionConfigs(List<ReportSectionConfigDto> reportSectionConfigs) {
        this.reportSectionConfigs = reportSectionConfigs;
    }
}
