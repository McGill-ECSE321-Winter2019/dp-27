package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.StudentReport;

public class ReportSectionDto {
    private int id;
    private String title;
    private String content;

    private StudentReport studentReport;
    private EmployerReport employerReport;

    public ReportSectionDto(
            int id,
            String title,
            String content,
            StudentReport studentReport,
            EmployerReport employerReport) {
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

    public StudentReport getStudentReport() {
        return this.studentReport;
    }

    public void setStudentReport(StudentReport studentReport) {
        this.studentReport = studentReport;
    }

    public EmployerReport getEmployerReport() {
        return this.employerReport;
    }

    public void setEmployerReport(EmployerReport employerReport) {
        this.employerReport = employerReport;
    }
}
