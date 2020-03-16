package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentReportSectionRepository;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentReportSectionService {

    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;
    @Autowired StudentReportSectionRepository studentReportSectionRepository;
    @Autowired StudentReportRepository studentReportRepository;

    /**
     * Creates new student report section in the database
     *
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return created report section
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
            throw new IllegalArgumentException(error.toString().trim());
        }

        StudentReportSection rs = new StudentReportSection();
        rs.setResponse(response.trim());
        rs.setReportSectionConfig(reportSectionConfig);
        rs.setStudentReport(studentReport);

        return studentReportSectionRepository.save(rs);
    }

    /**
     * Retrieves specified existing report section from database
     *
     * @param id
     * @return specifed report section
     */
    @Transactional
    public StudentReportSection getReportSection(int id) {
        StudentReportSection rs = studentReportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException(
                    "Student report section with ID " + id + " does not exist!");
        }

        return rs;
    }

    /**
     * Returns all student report sections from the database
     *
     * @return list of report sections
     */
    @Transactional
    public List<StudentReportSection> getAllReportSections() {
        return ServiceUtils.toList(studentReportSectionRepository.findAll());
    }

    /**
     * Updates existing report section in database
     *
     * @param rs
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return updated report section
     */
    @Transactional
    public StudentReportSection updateReportSection(
            StudentReportSection rs,
            String response,
            ReportSectionConfig reportSectionConfig,
            StudentReport studentReport) {
        StringBuilder error = new StringBuilder();
        if (rs == null) {
            error.append("Student report section cannot be null! ");
        }
        if (response != null && response.trim().length() == 0) {
            error.append("Response cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        if (response != null) {
            rs.setResponse(response.trim());
        }
        if (reportSectionConfig != null) {
            rs.setReportSectionConfig(reportSectionConfig);
        }
        if (studentReport != null) {
            rs.setStudentReport(studentReport);
        }

        return studentReportSectionRepository.save(rs);
    }

    /**
     * Delete specified report section from database
     *
     * @param rs
     * @return deleted report section
     */
    @Transactional
    public StudentReportSection deleteReportSection(StudentReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException("Student report section to delete cannot be null!");
        }

        // first delete from parents
        StudentReport sr = rs.getStudentReport();
        if (sr != null) {
            Set<StudentReportSection> sections = sr.getReportSections();
            sections.remove(rs);
            sr.setReportSections(sections);
            studentReportRepository.save(sr);
        }

        ReportSectionConfig rsConfig = rs.getReportSectionConfig();
        if (rsConfig != null) {
            Set<StudentReportSection> sections = rsConfig.getStudentReportSections();
            sections.remove(rs);
            rsConfig.setStudentReportSections(sections);
            reportSectionConfigRepository.save(rsConfig);
        }

        rs.setStudentReport(null);
        rs.setReportSectionConfig(null);
        studentReportSectionRepository.save(rs);

        studentReportSectionRepository.delete(rs);
        return rs;
    }
}
