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
     * @return created ReportSectionConfig
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
     * Returns all ReportSectionConfig response types
     *
     * @return an array of all the response types
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
            Integer questionNumber,
            ReportConfig reportConfig,
            Set<ReportSection> reportSections) {
        StringBuilder error = new StringBuilder();
        if (rsConfig == null) {
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
            rsConfig.setSectionPrompt(sectionPrompt);
        }
        if (responseType != null) {
            rsConfig.setResponseType(responseType);
        }
        if (questionNumber != null) {
            rsConfig.setQuestionNumber(questionNumber);
        }
        if (reportConfig != null) {
            rsConfig.setReportConfig(reportConfig);
        }
        if (reportSections != null) {
            rsConfig.setReportSections(reportSections);
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
        Set<ReportSectionConfig> rsConfigs = new HashSet<>(reportConfig.getReportSectionConfigs());
        rsConfigs.remove(rsConfig);
        reportConfig.setReportSectionConfigs(rsConfigs);
        reportConfigRepository.save(reportConfig);

        // update question numbers of other ReportSectionConfigs
        List<ReportSectionConfig> allReportSectionConfigs = getAllReportSectionConfigs();
        for (ReportSectionConfig rsc : allReportSectionConfigs) {
            if (rsc.getQuestionNumber() > rsConfig.getQuestionNumber()) {
                // lower question number by 1
                updateReportSectionConfig(
                        rsc, null, null, rsc.getQuestionNumber() - 1, null, null);
            }
        }

        reportSectionConfigRepository.delete(rsConfig);
        return rsConfig;
    }
}
