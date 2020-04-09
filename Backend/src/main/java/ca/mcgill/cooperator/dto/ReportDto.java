package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportStatus;
import java.util.List;

public class ReportDto {
    private Integer id;
    private String title;
    private ReportStatus status;

    private byte[] data;

    private CoopDto coop;
    private AuthorDto author;
    private List<ReportSectionDto> reportSections;

    public ReportDto() {}

    public ReportDto(
            Integer id,
            String title,
            ReportStatus status,
            byte[] data,
            CoopDto coop,
            AuthorDto author,
            List<ReportSectionDto> reportSections) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.data = data;
        this.coop = coop;
        this.author = author;
        this.reportSections = reportSections;
    }

    /*--- Getters and Setters ---*/

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
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

    public AuthorDto getAuthor() {
        return this.author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public List<ReportSectionDto> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSectionDto> reportSections) {
        this.reportSections = reportSections;
    }
}
