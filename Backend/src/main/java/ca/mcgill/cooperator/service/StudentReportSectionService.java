package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentReportSectionRepository;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentReportSectionService extends BaseService {

    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;
    @Autowired StudentReportSectionRepository studentReportSectionRepository;
    @Autowired StudentReportRepository studentReportRepository;

    /**
     * Creates a new StudentReportSection
     *
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return the created StudentReportSection
     */
    @Transactional
    public StudentReportSection createReportSection(
            String response, ReportSectionConfig reportSectionConfig, StudentReport studentReport) {
        StringBuilder error = new StringBuilder();
        if (response == null || response.trim().length() == 0) {
            error.append("Response cannot be empty! ");
        }
        if (reportSectionConfig == null) {
            error.append("Report section config cannot be null! ");
        }
        if (studentReport == null) {
            error.append("Student report cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        StudentReportSection rs = new StudentReportSection();
        rs.setResponse(response.trim());
        rs.setReportSectionConfig(reportSectionConfig);
        rs.setStudentReport(studentReport);

        return studentReportSectionRepository.save(rs);
    }

    /**
     * Gets an existing StudentReportSection by ID
     *
     * @param id
     * @return StudentReportSection with specified ID
     */
    @Transactional
    public StudentReportSection getReportSection(int id) {
        StudentReportSection rs = studentReportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student report section with ID " + id + " does not exist!");
        }

        return rs;
    }

    /**
     * Gets all StudentReportSections
     *
     * @return all StudentReportSections
     */
    @Transactional
    public List<StudentReportSection> getAllReportSections() {
        return ServiceUtils.toList(studentReportSectionRepository.findAll());
    }

    /**
     * Updates an existing StudentReportSection
     *
     * @param studentReportSection
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return the updated StudentReportSection
     */
    @Transactional
    public StudentReportSection updateReportSection(
            StudentReportSection studentReportSection,
            String response,
            ReportSectionConfig reportSectionConfig,
            StudentReport studentReport) {
        StringBuilder error = new StringBuilder();
        if (studentReportSection == null) {
            error.append("Student report section cannot be null! ");
        }
        if (response != null && response.trim().length() == 0) {
            error.append("Response cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (response != null) {
            studentReportSection.setResponse(response.trim());
        }
        if (reportSectionConfig != null) {
            studentReportSection.setReportSectionConfig(reportSectionConfig);
        }
        if (studentReport != null) {
            studentReportSection.setStudentReport(studentReport);
        }

        return studentReportSectionRepository.save(studentReportSection);
    }

    /**
     * Deletes an existing StudentReportSection
     *
     * @param studentReportSection
     * @return the deleted StudentReportSection
     */
    @Transactional
    public StudentReportSection deleteReportSection(StudentReportSection studentReportSection) {
        if (studentReportSection == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student report section to delete cannot be null!");
        }

        // first delete from parents
        StudentReport sr = studentReportSection.getStudentReport();
        if (sr != null) {
            // make a new Set to avoid pointer issues
            Set<StudentReportSection> sections = new HashSet<>(sr.getReportSections());
            sections.remove(studentReportSection);
            sr.setReportSections(sections);
            studentReportRepository.save(sr);
        }

        ReportSectionConfig rsConfig = studentReportSection.getReportSectionConfig();
        if (rsConfig != null) {
            Set<StudentReportSection> sections = new HashSet<>(rsConfig.getStudentReportSections());
            sections.remove(studentReportSection);
            rsConfig.setStudentReportSections(sections);
            reportSectionConfigRepository.save(rsConfig);
        }

        studentReportSection.setStudentReport(null);
        studentReportSection.setReportSectionConfig(null);
        studentReportSectionRepository.save(studentReportSection);

        studentReportSectionRepository.delete(studentReportSection);
        return studentReportSection;
    }
}
