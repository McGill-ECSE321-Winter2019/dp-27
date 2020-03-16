package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportStatus;
import java.util.List;

public class EmployerReportDto {
    private int id;
    private String title;
    private ReportStatus status;

    private byte[] data;

    private CoopDto coop;
    private EmployerContactDto employerContact;
    private List<EmployerReportSectionDto> reportSections;

    public EmployerReportDto() {}

    public EmployerReportDto(
            int id,
            String title,
            ReportStatus status,
            byte[] data,
            CoopDto coop,
            EmployerContactDto employerContact,
            List<EmployerReportSectionDto> reportSections) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.data = data;
        this.coop = coop;
        this.employerContact = employerContact;
        this.reportSections = reportSections;
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

    public EmployerContactDto getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContactDto employerContact) {
        this.employerContact = employerContact;
    }

    public List<EmployerReportSectionDto> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<EmployerReportSectionDto> reportSections) {
        this.reportSections = reportSections;
    }
}
