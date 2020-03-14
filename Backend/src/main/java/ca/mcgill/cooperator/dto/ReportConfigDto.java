package ca.mcgill.cooperator.dto;

import java.util.List;

public class ReportConfigDto {
    private int id;
    private boolean requiresFile;
    private int deadline;
    private boolean isDeadlineFromStart;
    private String type;

    private List<ReportSectionConfigDto> reportSectionConfigs;

    public ReportConfigDto() {}

    public ReportConfigDto(
            int id,
            boolean requiresFile,
            int deadline,
            boolean isDeadlineFromStart,
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

    public int getId() {
        return this.id;
    }

    public boolean getRequiresFile() {
        return this.requiresFile;
    }

    public void setRequiresFile(boolean requiresFile) {
        this.requiresFile = requiresFile;
    }

    public int getDeadline() {
        return this.deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public boolean getIsDeadlineFromStart() {
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
