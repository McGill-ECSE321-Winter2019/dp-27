package ca.mcgill.cooperator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ReportSection {
    @Id @GeneratedValue private int id;
    private String title;
    private String content;

    @ManyToOne(optional = true)
    private StudentReport studentReport;

    @ManyToOne(optional = true)
    private EmployerReport employerReport;
    
    /*--- Constructors ---*/
    
    public ReportSection(String title, String content, StudentReport stuReport, EmployerReport employerReport) {
    	this.title = title;
    	this.content = content;
    	this.studentReport = stuReport;
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
