package ca.mcgill.cooperator.dto;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import java.util.List;

public class EmployerReportDto {
    private int id;
    private ReportStatus status;

    private Coop coop;
    private EmployerContact employerContact;
    private List<ReportSection> reportSections;

    public EmployerReportDto(
            int id,
            ReportStatus status,
            Coop coop,
            EmployerContact employerContact,
            List<ReportSection> reportSections) {
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

    public Coop getCoop() {
        return this.coop;
    }

    public void setCoop(Coop coop) {
        this.coop = coop;
    }

    public EmployerContact getEmployerContact() {
        return this.employerContact;
    }

    public void setEmployerContact(EmployerContact employerContact) {
        this.employerContact = employerContact;
    }

    public List<ReportSection> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(List<ReportSection> reportSections) {
        this.reportSections = reportSections;
    }
}
