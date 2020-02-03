package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.StudentReport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentReportService {

    @Autowired StudentReportRepository studentReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired ReportSectionRepository reportSectionRepository;

    /**
     * Creates new student report in database
     *
     * @param status
     * @param c
     * @param file object
     * @return created student report
     */
    @Transactional
    public StudentReport createStudentReport(
            ReportStatus status, Coop c, String title, MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null! ");
        }
        if (title == null) {
            error.append("File title cannot be null! ");
        }
        if (file == null) {
            error.append("File cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        StudentReport sr = new StudentReport();

        sr.setStatus(status);
        sr.setCoop(c);
        sr.setReportSections(new ArrayList<ReportSection>());
        sr.setTitle(title);
        try {
            sr.setData(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        studentReportRepository.save(sr);

        Set<StudentReport> reports = c.getStudentReports();
        reports.add(sr);
        c.setStudentReports(reports);

        coopRepository.save(c);

        return studentReportRepository.save(sr);
    }

    /**
     * Retrieves specified existing student report from database
     *
     * @param id
     * @return specified student report
     */
    @Transactional
    public StudentReport getStudentReport(int id) {
        StudentReport sr = studentReportRepository.findById(id).orElse(null);
        if (sr == null) {
            throw new IllegalArgumentException("Student Report with ID " + id + " does not exist!");
        }

        return sr;
    }

    /**
     * Retrieves all student reports from database
     *
     * @return list of student reports
     */
    @Transactional
    public List<StudentReport> getAllStudentReports() {
        return ServiceUtils.toList(studentReportRepository.findAll());
    }
    /**
     * Updates existing student report in database
     *
     * @param sr
     * @param status
     * @param c
     * @param sections
     * @return updated student report
     */
    @Transactional
    public StudentReport updateStudentReport(
            StudentReport sr,
            ReportStatus status,
            String title,
            Coop c,
            List<ReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (sr == null) {
            error.append("Student Report cannot be null! ");
        }
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null! ");
        }
        if (title == null) {
            error.append("File title cannot be null! ");
        }
        if (file == null) {
            error.append("File cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        sr.setStatus(status);
        sr.setCoop(c);
        sr.setTitle(title);
        if (sections != null) {
            sr.setReportSections(sections);
        }
        try {
            sr.setData(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        studentReportRepository.save(sr);

        // add/set employer report to coop
        boolean coopContains = false;

        Set<StudentReport> coopReports = c.getStudentReports();
        for (StudentReport coopStudentReport : coopReports) {
            if (coopStudentReport.getId() == sr.getId()) {
                coopReports.remove(coopStudentReport);
                coopReports.add(sr);
                /*int index = coopReports.indexOf(coopStudentReport);
                coopReports.set(index, sr);*/
                coopContains = true;
            }
        }

        if (coopContains == false) {
            coopReports.add(sr);
        }
        c.setStudentReports(coopReports);

        coopRepository.save(c);

        // set employer report as parent for all report sections
        for (ReportSection section : sections) {
            section.setStudentReport(sr);
            reportSectionRepository.save(section);
        }

        return studentReportRepository.save(sr);
    }

    /**
     * Deletes specified student report from database
     *
     * @param sr
     * @return deleted student report
     */
    @Transactional
    public StudentReport deleteStudentReport(StudentReport sr) {
        if (sr == null) {
            throw new IllegalArgumentException("Student Report to delete cannot be null!");
        }

        // first delete from all parents
        Coop c = sr.getCoop();
        Set<StudentReport> coopReports = c.getStudentReports();
        coopReports.remove(sr);
        c.setStudentReports(coopReports);

        coopRepository.save(c);

        studentReportRepository.delete(sr);

        return sr;
    }
}
