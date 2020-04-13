package ca.mcgill.cooperator.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class CourseOffering {
    @Id @GeneratedValue private int id;
    private int year;
    private Season season;

    @ManyToOne(optional = false)
    private Course course;

    @OneToMany(
            mappedBy = "courseOffering",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Coop> coops;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(
  		   name = "course_offering_report_config",
  		  joinColumns = @JoinColumn(name = "course_offering_id", referencedColumnName = "id"), 
  		  inverseJoinColumns = @JoinColumn(name = "report_config_id", referencedColumnName = "id"))
    private Set<ReportConfig> reportConfigs;

    /*--- Getters and Setters ---*/

    public int getId() {
        return this.id;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Coop> getCoops() {
        return this.coops;
    }

    public void setCoops(Set<Coop> coops) {
        if (this.coops == null) {
            this.coops = coops;
        } else {
            this.coops.clear();
            this.coops.addAll(coops);
        }
    }
    
    public Set<ReportConfig> getReportConfigs() {
        return this.reportConfigs;
    }

    public void setReportConfigs(Set<ReportConfig> reportConfigs) {
        if (this.reportConfigs == null) {
            this.reportConfigs = reportConfigs;
        } else {
            this.reportConfigs.clear();
            this.reportConfigs.removeAll(this.reportConfigs);
            this.reportConfigs.addAll(reportConfigs);
        }
    }
    
    public void addReportConfig(ReportConfig reportConfig) {
        reportConfigs.add(reportConfig);
        reportConfig.getCourseOfferings().add(this);
    }

    public void removeReportConfig(ReportConfig reportConfig) {
        reportConfigs.remove(reportConfig);
        reportConfig.getCourseOfferings().remove(this);
    }  
}
