package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.EmployerReportSectionRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerReportSectionService extends BaseService {

    @Autowired EmployerReportSectionRepository employerReportSectionRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;

    /**
     * Creates new employer report section in the database
     *
     * @param response
     * @param reportSectionConfig
     * @param employerReport
     * @return created report section
     */
    @Transactional
    public EmployerReportSection createReportSection(
            String response,
            ReportSectionConfig reportSectionConfig,
            EmployerReport employerReport) {
        StringBuilder error = new StringBuilder();
        if (response == null || response.trim().length() == 0) {
            error.append("Response cannot be empty! ");
        }
        if (reportSectionConfig == null) {
            error.append("Report section config cannot be null! ");
        }
        if (employerReport == null) {
            error.append("Employer report cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        EmployerReportSection rs = new EmployerReportSection();
        rs.setResponse(response.trim());
        rs.setReportSectionConfig(reportSectionConfig);
        rs.setEmployerReport(employerReport);

        return employerReportSectionRepository.save(rs);
    }

    /**
     * Retrieves specified existing report section from database
     *
     * @param id
     * @return specifed report section
     */
    @Transactional
    public EmployerReportSection getReportSection(int id) {
        EmployerReportSection rs = employerReportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer report Section with ID " + id + " does not exist!");
        }

        return rs;
    }

    /**
     * Returns all employer report sections from the database
     *
     * @return list of report sections
     */
    @Transactional
    public List<EmployerReportSection> getAllReportSections() {
        return ServiceUtils.toList(employerReportSectionRepository.findAll());
    }

    /**
     * Updates existing report section in database
     *
     * @param rs
     * @param response
     * @param reportSectionConfig
     * @param employerReport
     * @return updated report section
     */
    @Transactional
    public EmployerReportSection updateReportSection(
            EmployerReportSection rs,
            String response,
            ReportSectionConfig reportSectionConfig,
            EmployerReport employerReport) {
        StringBuilder error = new StringBuilder();
        if (rs == null) {
            error.append("Employer report section cannot be null! ");
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
        if (employerReport != null) {
            rs.setEmployerReport(employerReport);
        }

        return employerReportSectionRepository.save(rs);
    }

    /**
     * Delete specified report section from database
     *
     * @param rs
     * @return deleted report section
     */
    @Transactional
    public EmployerReportSection deleteReportSection(EmployerReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer report section to delete cannot be null!");
        }

        // first delete from parents
        EmployerReport er = rs.getEmployerReport();
        if (er != null) {
            Set<EmployerReportSection> sections = er.getReportSections();
            sections.remove(rs);
            er.setReportSections(sections);
            employerReportRepository.save(er);
        }

        ReportSectionConfig rsConfig = rs.getReportSectionConfig();
        if (rsConfig != null) {
            Set<EmployerReportSection> sections = rsConfig.getEmployerReportSections();
            sections.remove(rs);
            rsConfig.setEmployerReportSections(sections);
            reportSectionConfigRepository.save(rsConfig);
        }

        rs.setEmployerReport(null);
        rs.setReportSectionConfig(null);
        employerReportSectionRepository.save(rs);

        employerReportSectionRepository.delete(rs);
        return rs;
    }
}
