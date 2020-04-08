package ca.mcgill.cooperator.dto;

public class ReportSectionDto {
	private Integer id;
    private String response;

    private ReportDto report;
    private ReportSectionConfigDto reportSectionConfig;

    public ReportSectionDto() {}

    public ReportSectionDto(
            Integer id,
            String response,
            ReportDto report,
            ReportSectionConfigDto reportSectionConfig) {
        this.id = id;
        this.response = response;
        this.report = report;
        this.reportSectionConfig = reportSectionConfig;
    }
    
    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ReportDto getReport() {
        return this.report;
    }

    public void setReport(ReportDto report) {
        this.report = report;
    }

    public ReportSectionConfigDto getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfigDto reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }

}
