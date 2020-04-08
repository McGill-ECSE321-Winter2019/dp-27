package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.ArrayList;
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
     * @return the created ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig createReportSectionConfig(
            String sectionPrompt,
            ReportResponseType responseType,
            int questionNumber,
            ReportConfig reportConfig) {
        StringBuilder error = new StringBuilder();
        if (sectionPrompt == null || sectionPrompt.trim().length() == 0) {
            error.append("Section prompt cannot be empty! ");
        }
        if (responseType == null) {
            error.append("Response type cannot be null! ");
        }
        if (questionNumber <= 0) {
            error.append("Question number cannot be less than 1! ");
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
        rsConfig.setQuestionNumber(questionNumber);
        rsConfig.setReportConfig(reportConfig);
        rsConfig.setReportSections(new HashSet<ReportSection>());

        return reportSectionConfigRepository.save(rsConfig);
    }

    /**
     * Gets an existing ReportSectionConfig by ID
     *
     * @param id
     * @return ReportSectionConfig with specified ID
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
     * Gets all ReportSectionConfigs
     *
     * @return all ReportSectionConfigs
     */
    @Transactional
    public List<ReportSectionConfig> getAllReportSectionConfigs() {
        return ServiceUtils.toList(reportSectionConfigRepository.findAll());
    }

    /**
     * Gets all ReportSectionConfig response types
     *
     * @return a list of all response types
     */
    @Transactional
    public List<String> getAllResponseTypes() {
        ReportResponseType[] responseTypes = ReportResponseType.values();
        List<String> responseTypeStrings = new ArrayList<>(responseTypes.length);

        for (ReportResponseType rt : responseTypes) {
            responseTypeStrings.add(rt.name());
        }

        return responseTypeStrings;
    }

    /**
     * Updates an existing ReportSectionConfig
     *
     * @param reportSectionConfig
     * @param sectionPrompt
     * @param responseType
     * @param reportConfig
     * @param employerReportSections
     * @param studentReportSections
     * @return the updated ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig updateReportSectionConfig(
            ReportSectionConfig reportSectionConfig,
            String sectionPrompt,
            ReportResponseType responseType,
            Integer questionNumber,
            ReportConfig reportConfig,
            Set<ReportSection> reportSections) {
        StringBuilder error = new StringBuilder();
        if (reportSectionConfig == null) {
            error.append("Report section config to update cannot be null! ");
        }
        if (sectionPrompt != null && sectionPrompt.trim().length() == 0) {
            error.append("Section prompt cannot be empty! ");
        }
        if (questionNumber != null && questionNumber <= 0) {
            error.append("Question number cannot be less than 1!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (sectionPrompt != null) {
            reportSectionConfig.setSectionPrompt(sectionPrompt);
        }
        if (responseType != null) {
            reportSectionConfig.setResponseType(responseType);
        }
        if (questionNumber != null) {
            reportSectionConfig.setQuestionNumber(questionNumber);
        }
        if (reportConfig != null) {
            reportSectionConfig.setReportConfig(reportConfig);
        }
        if (reportSections != null) {
        	reportSectionConfig.setReportSections(reportSections);
        }

        return reportSectionConfigRepository.save(reportSectionConfig);
    }

    /**
     * Deletes an existing ReportSectionConfig
     *
     * @param reportSectionConfig
     * @return the deleted ReportSectionConfig
     */
    @Transactional
    public ReportSectionConfig deleteReportSectionConfig(ReportSectionConfig reportSectionConfig) {
        if (reportSectionConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report section config to delete cannot be null!");
        }

        // first delete from parent ReportConfig
        ReportConfig reportConfig = reportSectionConfig.getReportConfig();
        Set<ReportSectionConfig> rsConfigs = new HashSet<>(reportConfig.getReportSectionConfigs());
        rsConfigs.remove(reportSectionConfig);
        reportConfig.setReportSectionConfigs(rsConfigs);
        reportConfigRepository.save(reportConfig);

        // update question numbers of other ReportSectionConfigs
        List<ReportSectionConfig> allReportSectionConfigs = getAllReportSectionConfigs();
        for (ReportSectionConfig rsc : allReportSectionConfigs) {
            if (rsc.getQuestionNumber() > reportSectionConfig.getQuestionNumber()) {
                // lower question number by 1
                updateReportSectionConfig(
                        rsc, null, null, rsc.getQuestionNumber() - 1, null, null);
            }
        }

        reportSectionConfigRepository.delete(reportSectionConfig);
        return reportSectionConfig;
    }
}
