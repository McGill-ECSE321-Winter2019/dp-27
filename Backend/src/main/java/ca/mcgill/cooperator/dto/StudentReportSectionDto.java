package ca.mcgill.cooperator.dto;

public class StudentReportSectionDto {
    private int id;
    private String response;

    private StudentReportDto studentReport;
    private ReportSectionConfigDto reportSectionConfig;

    public StudentReportSectionDto() {}

    public StudentReportSectionDto(
            int id,
            String response,
            StudentReportDto studentReport,
            ReportSectionConfigDto reportSectionConfig) {
        this.id = id;
        this.response = response;
        this.studentReport = studentReport;
        this.reportSectionConfig = reportSectionConfig;
    }

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

    public StudentReportDto getStudentReport() {
        return this.studentReport;
    }

    public void setStudentReport(StudentReportDto studentReport) {
        this.studentReport = studentReport;
    }

    public ReportSectionConfigDto getReportSectionConfig() {
        return this.reportSectionConfig;
    }

    public void setReportSectionConfig(ReportSectionConfigDto reportSectionConfig) {
        this.reportSectionConfig = reportSectionConfig;
    }
}
