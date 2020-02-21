package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.StudentReport;
import java.io.IOException;
import java.util.HashSet;
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
     * @param title
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
        if (title == null || title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        StudentReport sr = new StudentReport();

        sr.setStatus(status);
        sr.setCoop(c);
        sr.setReportSections(new HashSet<ReportSection>());
        sr.setTitle(title);
        if (file != null) {
	        try {
	            sr.setData(file.getBytes());
	        } catch (IOException e) {
	            throw new IllegalArgumentException(e.getMessage());
	        }
        }

        sr = studentReportRepository.save(sr);

        Set<StudentReport> reports = new HashSet<>();
        reports.addAll(c.getStudentReports());
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
            Set<ReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (sr == null) {
            error.append("Student Report cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        if (status != null) {
        	sr.setStatus(status);
        }
        if (c != null) {
        	sr.setCoop(c);
        }
        if (title != null && title.trim().length() > 0) {
        	sr.setTitle(title);
        }
        if (sections != null) {
            sr.setReportSections(sections);
        }
        
        if (file != null) {
	        try {
	            sr.setData(file.getBytes());
	        } catch (IOException e) {
	            throw new IllegalArgumentException(e.getMessage());
	        }
        }

        sr = studentReportRepository.save(sr);

        if (c != null) {
	        // add/set employer report to coop
	        boolean coopContains = false;
	
	        Set<StudentReport> coopReports = new HashSet<>();
	        coopReports.addAll(c.getStudentReports());
	        for (StudentReport coopStudentReport : coopReports) {
	            if (coopStudentReport.getId() == sr.getId()) {
	                coopReports.remove(coopStudentReport);
	                coopReports.add(sr);
	                coopContains = true;
	            }
	        }
	
	        if (coopContains == false) {
	            coopReports.add(sr);
	        }
	        c.setStudentReports(coopReports);
	
	        coopRepository.save(c);
        }

        if (sections != null) {
	        // set employer report as parent for all report sections
	        for (ReportSection section : sections) {
	            section.setStudentReport(sr);
	            reportSectionRepository.save(section);
	        }
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
