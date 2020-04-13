package ca.mcgill.cooperator.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class ReportConfig {
    @Id @GeneratedValue private int id;
    private boolean requiresFile;
    private int deadline;
    private boolean isDeadlineFromStart;

    @Column(unique = true)
    private String type;

    @OneToMany(
            mappedBy = "reportConfig",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ReportSectionConfig> reportSectionConfigs;
    
    @OneToMany(
            mappedBy = "reportConfig",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Report> reports;
    
    @ManyToMany(
    		mappedBy = "reportConfigs",
            fetch = FetchType.EAGER)
    private Set<CourseOffering> courseOfferings;
    

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public boolean getRequiresFile() {
        return this.requiresFile;
    }

    public void setRequiresFile(boolean requiresFile) {
        this.requiresFile = requiresFile;
    }

    public int getDeadline() {
        return this.deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public boolean getIsDeadlineFromStart() {
        return this.isDeadlineFromStart;
    }

    public void setIsDeadlineFromStart(boolean isDeadlineFromStart) {
        this.isDeadlineFromStart = isDeadlineFromStart;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<ReportSectionConfig> getReportSectionConfigs() {
        return this.reportSectionConfigs;
    }

    public void setReportSectionConfigs(Set<ReportSectionConfig> reportSectionConfigs) {
        if (this.reportSectionConfigs == null) {
            this.reportSectionConfigs = reportSectionConfigs;
        } else {
            this.reportSectionConfigs.clear();
            this.reportSectionConfigs.addAll(reportSectionConfigs);
        }
    }
    
    public Set<Report> getReports() {
        return this.reports;
    }

    public void setReports(Set<Report> reports) {
        if (this.reports == null) {
            this.reports = reports;
        } else {
            this.reports.clear();
            this.reports.addAll(reports);
        }
    }
    
    public Set<CourseOffering> getCourseOfferings() {
        return this.courseOfferings;
    }

    public void setCourseOfferings(Set<CourseOffering> courseOfferings) {
        if (this.courseOfferings == null) {
            this.courseOfferings = courseOfferings;
        } else {
            this.courseOfferings.clear();
            this.courseOfferings.addAll(courseOfferings);
        }
    }
}
