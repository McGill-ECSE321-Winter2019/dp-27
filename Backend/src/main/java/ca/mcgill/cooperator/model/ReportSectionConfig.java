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
    private int questionNumber;

    @ManyToOne(optional = false)
    private ReportConfig reportConfig;

    @OneToMany(
            mappedBy = "reportSectionConfig",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ReportSection> reportSections;

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

    public int getQuestionNumber() {
        return this.questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public ReportConfig getReportConfig() {
        return this.reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public Set<ReportSection> getReportSections() {
        return this.reportSections;
    }

    public void setReportSections(Set<ReportSection> reportSections) {
        if (this.reportSections == null) {
            this.reportSections = reportSections;
        } else {
            this.reportSections.clear();
            this.reportSections.addAll(reportSections);
        }
    }
}
