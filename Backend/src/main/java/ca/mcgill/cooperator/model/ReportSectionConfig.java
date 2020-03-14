package ca.mcgill.cooperator.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class ReportSectionConfig {
    @Id @GeneratedValue private int id;
    private String sectionPrompt;
    private ReportResponseType responseType;

    @ManyToOne(optional = false)
    private ReportConfig reportConfig;

    @OneToMany(
            mappedBy = "reportSectionConfig",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<EmployerReportSection> employerReportSections;

    @OneToMany(
            mappedBy = "reportSectionConfig",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<StudentReportSection> studentReportSections;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public String getSectionPrompt() {
        return this.sectionPrompt;
    }

    public void setSectionPrompt(String sectionPrompt) {
        this.sectionPrompt = sectionPrompt;
    }

    public ReportResponseType getResponseType() {
        return this.responseType;
    }

    public void setResponseType(ReportResponseType responseType) {
        this.responseType = responseType;
    }

    public ReportConfig getReportConfig() {
        return this.reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public Set<EmployerReportSection> getEmployerReportSections() {
        return this.employerReportSections;
    }

    public void setEmployerReportSections(Set<EmployerReportSection> employerReportSections) {
        if (this.employerReportSections == null) {
            this.employerReportSections = employerReportSections;
        } else {
            this.employerReportSections.clear();
            this.employerReportSections.addAll(employerReportSections);
        }
    }

    public Set<StudentReportSection> getStudentReportSections() {
        return this.studentReportSections;
    }

    public void setStudentReportSections(Set<StudentReportSection> studentReportSections) {
        if (this.studentReportSections == null) {
            this.studentReportSections = studentReportSections;
        } else {
            this.studentReportSections.clear();
            this.studentReportSections.addAll(studentReportSections);
        }
    }
}
