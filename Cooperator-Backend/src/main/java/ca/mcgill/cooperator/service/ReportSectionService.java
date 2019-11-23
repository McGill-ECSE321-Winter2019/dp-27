package ca.mcgill.cooperator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.StudentReport;

@Service
public class ReportSectionService {
	
	@Autowired ReportSectionRepository reportSectionRepository;
	@Autowired StudentReportRepository studentReportRepository;
	@Autowired EmployerReportRepository employerReportRepository;
	
	@Transactional
	public ReportSection createReportSection(String title, String content) {
		StringBuilder error = new StringBuilder();
        if (title == null || title.trim().length() == 0) {
            error.append("Title cannot be null! ");
        }
        if (content == null || content.trim().length() == 0) {
            error.append("Content cannot be null! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        
        ReportSection rs = new ReportSection();
        rs.setTitle(title.trim());
        rs.setContent(content.trim());
        
        return reportSectionRepository.save(rs);
	}
	
	@Transactional
	public ReportSection getReportSection(int id) {
		ReportSection rs = reportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException("Report Section with ID " + id + " does not exist!");
        }

        return rs;
	}
	
	@Transactional
	public List<ReportSection> getAllReportSections() {
		return ServiceUtils.toList(reportSectionRepository.findAll());
	}
	
	@Transactional
	public ReportSection updateReportSection(ReportSection rs, String title, String content, StudentReport sr, EmployerReport er) {
		StringBuilder error = new StringBuilder();
		if (rs == null) {
			error.append("Report Section cannot be null! ");
		}
        if (title == null || title.trim().length() == 0) {
            error.append("Title cannot be null! ");
        }
        if (content == null || content.trim().length() == 0) {
            error.append("Content cannot be null! ");
        }
        if (sr != null && er != null) {
        	error.append("Cannot add to both Student Report and Employer Report!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        
        rs.setTitle(title.trim());
        rs.setContent(content.trim());
        
        if (sr != null) {
        	rs.setStudentReport(sr);
        	reportSectionRepository.save(rs);
        	boolean contains = false;
        	List<ReportSection> sections = sr.getReportSections();
        	for (ReportSection section : sections) {
        		if (section.getId() == rs.getId()) {
        			int index = sections.indexOf(section);
        			sections.set(index, rs);
        			contains = true;
        		}
        	}
        	if (contains == false) {
        		sections.add(rs);
        	}
        	sr.setReportSections(sections);
        	studentReportRepository.save(sr);
        }
        
        if (er != null) {
        	rs.setEmployerReport(er);
        	reportSectionRepository.save(rs);
        	boolean contains = false;
        	List<ReportSection> sections = er.getReportSections();
        	for (ReportSection section : sections) {
        		if (section.getId() == rs.getId()) {
        			int index = sections.indexOf(section);
        			sections.set(index, rs);
        			contains = true;
        		}
        	}
        	if (contains == false) {
        		sections.add(rs);
        	}
        	er.setReportSections(sections);
        	employerReportRepository.save(er);
        }
        
        return reportSectionRepository.save(rs);
	}
	
	@Transactional
	public ReportSection deleteReportSection(ReportSection rs) {
		if (rs == null) {
			throw new IllegalArgumentException("Report Section to delete cannot be null!");
		}
		
		//first delete from all parents
		StudentReport sr = rs.getStudentReport();
		if (sr != null) {
			List<ReportSection> sections = sr.getReportSections();
			sections.remove(rs);
			sr.setReportSections(sections);
			studentReportRepository.save(sr);
		}
		
		EmployerReport er = rs.getEmployerReport();
		if (er != null) {
			List<ReportSection> sections = er.getReportSections();
			sections.remove(rs);
			er.setReportSections(sections);
			employerReportRepository.save(er);
		}
		
		reportSectionRepository.delete(rs);
		return rs;
	}
}
