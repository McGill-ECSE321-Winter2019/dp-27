package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.EmployerReportSectionRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.HashSet;
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
     * Creates a new EmployerReportSection
     *
     * @param response
     * @param reportSectionConfig
     * @param employerReport
     * @return the created EmployerReportSection
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
     * Gets a EmployerReportSection by ID
     *
     * @param id
     * @return EmployerReportSection with specified ID
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
     * Gets all EmployerReportSections
     *
     * @return all EmployerReportSection
     */
    @Transactional
    public List<EmployerReportSection> getAllReportSections() {
        return ServiceUtils.toList(employerReportSectionRepository.findAll());
    }

    /**
     * Updates an existing EmployerReportSection
     *
     * @param employerReportSection
     * @param response
     * @param reportSectionConfig
     * @param employerReport
     * @return the updated EmployerReportSection
     */
    @Transactional
    public EmployerReportSection updateReportSection(
            EmployerReportSection employerReportSection,
            String response,
            ReportSectionConfig reportSectionConfig,
            EmployerReport employerReport) {
        StringBuilder error = new StringBuilder();
        if (employerReportSection == null) {
            error.append("Employer report section cannot be null! ");
        }
        if (response != null && response.trim().length() == 0) {
            error.append("Response cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (response != null) {
            employerReportSection.setResponse(response.trim());
        }
        if (reportSectionConfig != null) {
            employerReportSection.setReportSectionConfig(reportSectionConfig);
        }
        if (employerReport != null) {
            employerReportSection.setEmployerReport(employerReport);
        }

        return employerReportSectionRepository.save(employerReportSection);
    }

    /**
     * Deletes an existing EmployerReportSection
     *
     * @param employerReportSection
     * @return the deleted EmployerReportSection
     */
    @Transactional
    public EmployerReportSection deleteReportSection(EmployerReportSection employerReportSection) {
        if (employerReportSection == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer report section to delete cannot be null!");
        }

        // first delete from parents
        EmployerReport er = employerReportSection.getEmployerReport();
        if (er != null) {
            // make a new HashSet to avoid pointer issues
            Set<EmployerReportSection> sections = new HashSet<>(er.getReportSections());
            sections.remove(employerReportSection);
            er.setReportSections(sections);
            employerReportRepository.save(er);
        }

        ReportSectionConfig rsConfig = employerReportSection.getReportSectionConfig();
        if (rsConfig != null) {
            Set<EmployerReportSection> sections =
                    new HashSet<>(rsConfig.getEmployerReportSections());
            sections.remove(employerReportSection);
            rsConfig.setEmployerReportSections(sections);
            reportSectionConfigRepository.save(rsConfig);
        }

        employerReportSection.setEmployerReport(null);
        employerReportSection.setReportSectionConfig(null);
        employerReportSectionRepository.save(employerReportSection);

        employerReportSectionRepository.delete(employerReportSection);
        return employerReportSection;
    }
}
