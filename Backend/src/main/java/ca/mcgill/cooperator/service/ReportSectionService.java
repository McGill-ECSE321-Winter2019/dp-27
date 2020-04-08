package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportSection;

@Service
public class ReportSectionService extends BaseService {
	@Autowired ReportSectionRepository reportSectionRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;
    
    /**
     * Creates new report section in the database
     *
     * @param response
     * @param reportSectionConfig
     * @param report
     * @return created report section
     */
    @Transactional
    public ReportSection createReportSection(
            String response,
            ReportSectionConfig reportSectionConfig,
            Report report) {
        StringBuilder error = new StringBuilder();
        if (response == null || response.trim().length() == 0) {
            error.append("Response cannot be empty! ");
        }
        if (reportSectionConfig == null) {
            error.append("Report section config cannot be null! ");
        }
        if (report == null) {
            error.append("Report cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        ReportSection rs = new ReportSection();
        rs.setResponse(response.trim());
        rs.setReportSectionConfig(reportSectionConfig);
        rs.setReport(report);

        return reportSectionRepository.save(rs);
    }
    
    /**
     * Gets a EmployerReportSection by ID
     *
     * @param id
     * @return EmployerReportSection with specified ID
     */
    @Transactional
    public ReportSection getReportSection(int id) {
        ReportSection rs = reportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report Section with ID " + id + " does not exist!");
        }

        return rs;
    }
    
    /**
     * Returns all report sections from the database
     *
     * @return all EmployerReportSection
     */
    @Transactional
    public List<ReportSection> getAllReportSections() {
        return ServiceUtils.toList(reportSectionRepository.findAll());
    }
    
    /**
     * Updates an existing EmployerReportSection
     *
     * @param employerReportSection
     * @param response
     * @param reportSectionConfig
<<<<<<< HEAD:Backend/src/main/java/ca/mcgill/cooperator/service/ReportSectionService.java
     * @param report
     * @return updated report section
     */
    @Transactional
    public ReportSection updateReportSection(
            ReportSection rs,
            String response,
            ReportSectionConfig reportSectionConfig,
            Report report) {
        StringBuilder error = new StringBuilder();
        if (rs == null) {
            error.append("Report section cannot be null! ");
        }
        if (response != null && response.trim().length() == 0) {
            error.append("Response cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (response != null) {
        	rs.setResponse(response.trim());
        }
        if (reportSectionConfig != null) {
        	rs.setReportSectionConfig(reportSectionConfig);
        }
        if (report != null) {
            rs.setReport(report);
        }

        return reportSectionRepository.save(rs);
    }
    
    /**
     * Deletes an existing EmployerReportSection
     *
     * @param employerReportSection
     * @return the deleted EmployerReportSection
     */
    @Transactional
    public ReportSection deleteReportSection(ReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section to delete cannot be null!");
        }

        // first delete from parents
        Report r = rs.getReport();
        if (r != null) {
            Set<ReportSection> sections = r.getReportSections();
            sections.remove(rs);
            r.setReportSections(sections);
            reportRepository.save(r);
        }

        ReportSectionConfig rsConfig = rs.getReportSectionConfig();
        if (rsConfig != null) {
            Set<ReportSection> sections = rsConfig.getReportSections();
            sections.remove(rs);
            rsConfig.setReportSections(sections);
            reportSectionConfigRepository.save(rsConfig);
        }

        rs.setReport(null);
        rs.setReportSectionConfig(null);
        reportSectionRepository.save(rs);

        reportSectionRepository.delete(rs);
        return rs;
    }
    
    
    
}
