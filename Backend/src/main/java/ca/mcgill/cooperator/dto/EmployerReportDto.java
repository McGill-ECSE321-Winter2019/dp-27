package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.ReportStatus;
import java.util.List;

public class EmployerReportDto {
    private int id;
    private ReportStatus status;

    private CoopDto coop;
    private EmployerContactDto employerContact;
    private List<ReportSectionDto> reportSections;

    public EmployerReportDto(
            int id,
            ReportStatus status,
            CoopDto coop,
            EmployerContactDto employerContact,
            List<ReportSectionDto> reportSections) {
        this.id = id;
        this.status = status;
        this.coop = coop;
        this.employerContact = employerContact;
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

    public List<ReportSectionDto> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSectionDto> reportSections) {
        this.reportSections = reportSections;
    }
}
