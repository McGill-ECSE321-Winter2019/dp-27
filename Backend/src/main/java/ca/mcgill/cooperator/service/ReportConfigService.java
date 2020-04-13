package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportConfigService extends BaseService {

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

    /**
     * Creates a new ReportConfig
     *
     * @param requiresFile
     * @param deadline
     * @param isDeadlineFromStart
     * @param type
     * @return the created ReportConfig
     */
    @Transactional
    public ReportConfig createReportConfig(
            boolean requiresFile, int deadline, boolean isDeadlineFromStart, String type, Set<CourseOffering> courseOfferings) {
        StringBuilder error = new StringBuilder();
        if (deadline < 0) {
            error.append("Deadline cannot be negative! ");
        }
        if (type == null || type.trim().length() == 0) {
            error.append("Report type cannot be empty! ");
        }
        if (courseOfferings == null || courseOfferings.size() == 0) {
        	error.append("Course Offerings cannot be null or empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        ReportConfig reportConfig = new ReportConfig();
        reportConfig.setRequiresFile(requiresFile);
        reportConfig.setDeadline(deadline);
        reportConfig.setIsDeadlineFromStart(isDeadlineFromStart);
        reportConfig.setType(type);
        reportConfig.setCourseOfferings(courseOfferings);
        reportConfig.setReportSectionConfigs(new HashSet<ReportSectionConfig>());
        reportConfig.setReports(new HashSet<Report>());
        reportConfig = reportConfigRepository.save(reportConfig);
        
        Set<ReportConfig> reportConfigs = new HashSet<ReportConfig>();
        for (CourseOffering courseOffering : courseOfferings) {
        	reportConfigs = new HashSet<ReportConfig>(courseOffering.getReportConfigs());
        	reportConfigs.add(reportConfig);
        	courseOffering.setReportConfigs(reportConfigs);
        	courseOffering = courseOfferingRepository.save(courseOffering);
        }
        
        return reportConfigRepository.save(reportConfig);
    }

    /**
     * Gets the ReportConfig with specified ID
     *
     * @param id
     * @return ReportConfig with specified ID
     */
    @Transactional
    public ReportConfig getReportConfig(int id) {
        ReportConfig reportConfig = reportConfigRepository.findById(id).orElse(null);
        if (reportConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report config with ID " + id + " does not exist!");
        }

        return reportConfig;
    }

    /**
     * Gets the ReportConfig with specified type
     *
     * @param type
     * @return ReportConfig with specified type
     */
    @Transactional
    public ReportConfig getReportConfig(String type) {
        ReportConfig reportConfig = reportConfigRepository.findByType(type);
        if (reportConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report config with type " + type + " does not exist!");
        }

        return reportConfig;
    }

    /**
     * Gets all ReportConfigs
     *
     * @return all ReportConfigs
     */
    @Transactional
    public List<ReportConfig> getAllReportConfigs() {
        return ServiceUtils.toList(reportConfigRepository.findAll());
    }

    /**
     * Utility method to get all report types
     *
     * @return all report types
     */
    @Transactional
    public List<String> getAllReportTypes() {
        List<ReportConfig> reportConfigs = ServiceUtils.toList(reportConfigRepository.findAll());
        List<String> types = new ArrayList<>();
        for (ReportConfig rc : reportConfigs) {
            types.add(rc.getType());
        }
        return types;
    }

    /**
     * Updates an existing ReportConfig
     *
     * @param reportConfig
     * @param requiresFile
     * @param deadline
     * @param isDeadlineFromStart
     * @param type
     * @param reportSectionConfigs
     * @return the updated ReportConfig
     */
    @Transactional
    public ReportConfig updateReportConfig(
            ReportConfig reportConfig,
            Boolean requiresFile,
            Integer deadline,
            Boolean isDeadlineFromStart,
            String type,
            Set<ReportSectionConfig> reportSectionConfigs,
            Set<CourseOffering> courseOfferings,
            Set<Report> reports) {
        StringBuilder error = new StringBuilder();
        if (reportConfig == null) {
            error.append("Report config to update cannot be null! ");
        }
        if (deadline != null && deadline < 0) {
            error.append("Deadline cannot be negative! ");
        }
        if (type != null && type.trim().length() == 0) {
            error.append("Report type cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (requiresFile != null) {
            reportConfig.setRequiresFile(requiresFile);
        }
        if (deadline != null) {
            reportConfig.setDeadline(deadline);
        }
        if (isDeadlineFromStart != null) {
            reportConfig.setIsDeadlineFromStart(isDeadlineFromStart);
        }
        if (type != null) {
            reportConfig.setType(type);
        }
        if (reportSectionConfigs != null) {
            reportConfig.setReportSectionConfigs(reportSectionConfigs);
        }
        if (courseOfferings != null) {
        	//update course offerings
        	reportConfig.setCourseOfferings(courseOfferings);
        	//update from other side
        	for (CourseOffering courseOffering : courseOfferings) {
        		//get current report configs associated with course offering
            	Set<ReportConfig> reportConfigs = new HashSet<ReportConfig>(courseOffering.getReportConfigs());
            	//if report config already exists, remove it
            	reportConfigs.removeIf(rc -> (rc.getId() == reportConfig.getId()));
            	//re add report config
            	reportConfigs.add(reportConfig);
            	//delete all current course offering report configs so that they can be reset
            	courseOfferingRepository.deleteAllReportConfigsById(courseOffering.getId());
            	//set all new report configs for course offering
            	courseOffering.setReportConfigs(reportConfigs);
            	courseOfferingRepository.save(courseOffering);
            }
        	
        }
        if (reports != null) {
        	reportConfig.setReports(reports);
        }

        return reportConfigRepository.save(reportConfig);
    }

    /**
     * Deletes an existing ReportConfig
     *
     * @param reportConfig
     * @return the deleted ReportConfig
     */
    @Transactional
    public ReportConfig deleteReportConfig(ReportConfig reportConfig) {
        if (reportConfig == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report config to delete cannot be null!");
        }

        //delete report config from course offering
        Set<CourseOffering> courseOfferings = reportConfig.getCourseOfferings();
        for (CourseOffering courseOffering : courseOfferings) {
    		//get current report configs associated with course offering
        	Set<ReportConfig> reportConfigs = new HashSet<ReportConfig>(courseOffering.getReportConfigs());
        	//if report config exists, remove it
        	reportConfigs.removeIf(rc -> (rc.getId() == reportConfig.getId()));
        	//delete all current course offering report configs so that they can be reset
        	courseOfferingRepository.deleteAllReportConfigsById(courseOffering.getId());
        	//set all new report configs for course offering minus report config being deleted
        	courseOffering.setReportConfigs(reportConfigs);
        	courseOfferingRepository.save(courseOffering);
        }
        reportConfigRepository.delete(reportConfig);
        return reportConfig;
    }
}
