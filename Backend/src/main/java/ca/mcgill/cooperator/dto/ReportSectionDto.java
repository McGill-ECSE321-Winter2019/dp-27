package ca.mcgill.cooperator.dto;

public class ReportSectionDto {
    private int id;
    private String title;
    private String content;

    private StudentReportDto studentReport;
    private EmployerReportDto employerReport;

    public ReportSectionDto(
            int id,
            String title,
            String content,
            StudentReportDto studentReport,
            EmployerReportDto employerReport) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.studentReport = studentReport;
        this.employerReport = employerReport;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StudentReportDto getStudentReport() {
        return this.studentReport;
    }

    public void setStudentReport(StudentReportDto studentReport) {
        this.studentReport = studentReport;
    }

    public EmployerReportDto getEmployerReport() {
        return this.employerReport;
    }

    public void setEmployerReport(EmployerReportDto employerReport) {
        this.employerReport = employerReport;
    }
}
