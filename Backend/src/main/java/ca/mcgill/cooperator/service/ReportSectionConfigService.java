package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportSectionConfigService extends BaseService {

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;

    /**
     * Creates a new ReportSectionConfig
     *
     * @param sectionPrompt
     * @param responseType
     * @param reportConfig
     * @return created ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig createReportSectionConfig(
            String sectionPrompt, ReportResponseType responseType, ReportConfig reportConfig) {
        StringBuilder error = new StringBuilder();
        if (sectionPrompt == null || sectionPrompt.trim().length() == 0) {
            error.append("Section prompt cannot be empty! ");
        }
        if (responseType == null) {
            error.append("Response type cannot be null! ");
        }
        if (reportConfig == null) {
            error.append("Report config cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        ReportSectionConfig rsConfig = new ReportSectionConfig();
        rsConfig.setSectionPrompt(sectionPrompt);
        rsConfig.setResponseType(responseType);
        rsConfig.setReportConfig(reportConfig);
        rsConfig.setEmployerReportSections(new HashSet<EmployerReportSection>());
        rsConfig.setStudentReportSections(new HashSet<StudentReportSection>());

        return reportSectionConfigRepository.save(rsConfig);
    }

    /**
     * Retrieves an existing ReportSectionConfig by ID
     *
     * @param id
     * @return ReportSectionConfig with matching ID
     */
    @Transactional
    public ReportSectionConfig getReportSectionConfig(int id) {
        ReportSectionConfig rsConfig = reportSectionConfigRepository.findById(id).orElse(null);
        if (rsConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section config with ID " + id + " does not exist!");
        }

        return rsConfig;
    }

    /**
     * Retrieves all ReportSectionConfigs
     *
     * @return all ReportSectionConfigs
     */
    @Transactional
    public List<ReportSectionConfig> getAllReportSectionConfigs() {
        return ServiceUtils.toList(reportSectionConfigRepository.findAll());
    }

    /**
     * Updates an existing ReportSectionConfig
     *
     * @param rsConfig
     * @param sectionPrompt
     * @param responseType
     * @param reportConfig
     * @param employerReportSections
     * @param studentReportSections
     * @return the updated ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig updateReportSectionConfig(
            ReportSectionConfig rsConfig,
            String sectionPrompt,
            ReportResponseType responseType,
            ReportConfig reportConfig,
            Set<EmployerReportSection> employerReportSections,
            Set<StudentReportSection> studentReportSections) {
        StringBuilder error = new StringBuilder();
        if (rsConfig == null) {
            error.append("Report section config to update cannot be null! ");
        }
        if (sectionPrompt != null && sectionPrompt.trim().length() == 0) {
            error.append("Section prompt cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (sectionPrompt != null) {
            rsConfig.setSectionPrompt(sectionPrompt);
        }
        if (responseType != null) {
            rsConfig.setResponseType(responseType);
        }
        if (reportConfig != null) {
            rsConfig.setReportConfig(reportConfig);
        }
        if (employerReportSections != null) {
            rsConfig.setEmployerReportSections(employerReportSections);
        }
        if (studentReportSections != null) {
            rsConfig.setStudentReportSections(studentReportSections);
        }

        return reportSectionConfigRepository.save(rsConfig);
    }

    /**
     * Deletes an existing ReportSectionConfig
     *
     * @param rsConfig
     * @return the deleted ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig deleteReportSectionConfig(ReportSectionConfig rsConfig) {
        if (rsConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section config to delete cannot be null!");
        }

        // first delete from parent ReportConfig
        ReportConfig reportConfig = rsConfig.getReportConfig();
        Set<ReportSectionConfig> rsConfigs = reportConfig.getReportSectionConfigs();
        rsConfigs.remove(rsConfig);
        reportConfig.setReportSectionConfigs(rsConfigs);
        reportConfigRepository.save(reportConfig);

        rsConfig.setReportConfig(null);
        reportSectionConfigRepository.save(rsConfig);

        reportSectionConfigRepository.delete(rsConfig);
        return rsConfig;
    }
}
