package ca.mcgill.cooperator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.model.ReportSection;

@Service
public class ReportSectionService {
	
	@Autowired ReportSectionRepository reportSectionRepository;
	
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
        rs.setTitle(title);
        rs.setContent(content);
        
        return reportSectionRepository.save(rs);
	}
}
