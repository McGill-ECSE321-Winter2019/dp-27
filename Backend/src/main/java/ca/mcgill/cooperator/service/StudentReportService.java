package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentReportSectionRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentReportService extends BaseService {

    @Autowired CoopRepository coopRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired StudentReportSectionRepository studentReportSectionRepository;

    /**
     * Creates a new StudentReport
     *
     * @param status
     * @param coop
     * @param title
     * @param file object
     * @return the created StudentReport
     */
    @Transactional
    public StudentReport createStudentReport(
            ReportStatus status, Coop coop, String title, MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (coop == null) {
            error.append("Coop cannot be null! ");
        }
        if (title == null || title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        StudentReport sr = new StudentReport();

        sr.setStatus(status);
        sr.setCoop(coop);
        sr.setReportSections(new HashSet<StudentReportSection>());
        sr.setTitle(title);
        if (file != null) {
            try {
                sr.setData(file.getBytes());
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return studentReportRepository.save(sr);
    }

    /**
     * Gets an existing StudentReport by ID
     *
     * @param id
     * @return StudentReport with specified ID
     */
    @Transactional
    public StudentReport getStudentReport(int id) {
        StudentReport sr = studentReportRepository.findById(id).orElse(null);
        if (sr == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student Report with ID " + id + " does not exist!");
        }

        return sr;
    }

    /**
     * Gets all StudentReports
     *
     * @return all StudentReports
     */
    @Transactional
    public List<StudentReport> getAllStudentReports() {
        return ServiceUtils.toList(studentReportRepository.findAll());
    }
    /**
     * Updates an existing StudentReport
     *
     * @param studentReport
     * @param status
     * @param coop
     * @param sections
     * @return the updated StudentReport
     */
    @Transactional
    public StudentReport updateStudentReport(
            StudentReport studentReport,
            ReportStatus status,
            String title,
            Coop coop,
            Set<StudentReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (studentReport == null) {
            error.append("Student Report cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (status != null) {
            studentReport.setStatus(status);
        }
        if (coop != null) {
            studentReport.setCoop(coop);
        }
        if (title != null) {
            studentReport.setTitle(title);
        }
        if (sections != null) {
            studentReport.setReportSections(sections);
        }

        if (file != null) {
            try {
                studentReport.setData(file.getBytes());
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        studentReport = studentReportRepository.save(studentReport);

        // update student report side of relation since it doesn't sync
        if (sections != null) {
            // set student report as parent for all report sections
            for (StudentReportSection section : sections) {
                section.setStudentReport(studentReport);
                studentReportSectionRepository.save(section);
            }
        }

        return studentReportRepository.save(studentReport);
    }

    /**
     * Deletes an existing StudentReport
     *
     * @param studentReport
     * @return the deleted StudentReport
     */
    @Transactional
    public StudentReport deleteStudentReport(StudentReport studentReport) {
        if (studentReport == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Student Report to delete cannot be null!");
        }

        // first delete from all parents
        Coop c = studentReport.getCoop();
        // make a new Set to avoid pointer issues
        Set<StudentReport> coopReports = new HashSet<>(c.getStudentReports());
        coopReports.remove(studentReport);
        c.setStudentReports(coopReports);

        coopRepository.save(c);

        studentReportRepository.delete(studentReport);
        return studentReport;
    }
}
