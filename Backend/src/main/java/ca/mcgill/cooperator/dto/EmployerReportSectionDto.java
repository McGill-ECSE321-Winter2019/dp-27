package ca.mcgill.cooperator.dto;

public class EmployerReportSectionDto {
    private Integer id;
    private String response;

    private EmployerReportDto employerReport;
    private ReportSectionConfigDto reportSectionConfig;

    public EmployerReportSectionDto() {}

    public EmployerReportSectionDto(
            Integer id,
            String response,
            EmployerReportDto employerReport,
            ReportSectionConfigDto reportSectionConfig) {
        this.id = id;
        this.response = response;
        this.employerReport = employerReport;
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

    public EmployerReportDto getEmployerReport() {
        return this.employerReport;
    }

    public void setEmployerReport(EmployerReportDto employerReport) {
        this.employerReport = employerReport;
    }

    public ReportSectionConfigDto getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfigDto reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }
}
