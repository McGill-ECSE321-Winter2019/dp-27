package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportStatus;
import java.util.List;

public class StudentReportDto {
    private int id;
    private ReportStatus status;
    private String title;

    private byte[] data;

    private CoopDto coop;
    private List<ReportSectionDto> reportSections;

    public StudentReportDto(
            int id,
            ReportStatus status,
            String title,
            byte[] data,
            CoopDto coop,
            List<ReportSectionDto> reportSections) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.data = data;
        this.coop = coop;
        this.reportSections = reportSections;
    }

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public CoopDto getCoop() {
        return this.coop;
    }

    public void setCoop(CoopDto coop) {
        this.coop = coop;
    }

    public List<ReportSectionDto> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSectionDto> reportSections) {
        this.reportSections = reportSections;
    }
}
