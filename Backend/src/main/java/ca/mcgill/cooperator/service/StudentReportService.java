package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.StudentReport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentReportService {

    @Autowired StudentReportRepository studentReportRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired ReportSectionRepository reportSectionRepository;

    /**
     * creates new student report in database
     *
     * @param status
     * @param c
     * @return created student report
     */
    @Transactional
    public StudentReport createStudentReport(ReportStatus status, Coop c) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        StudentReport sr = new StudentReport();

        sr.setStatus(status);
        sr.setCoop(c);
        sr.setReportSections(new ArrayList<ReportSection>());

        studentReportRepository.save(sr);

        List<StudentReport> reports = c.getStudentReports();
        reports.add(sr);
        c.setStudentReports(reports);

        coopRepository.save(c);

        return studentReportRepository.save(sr);
    }

    /**
     * retrieve specified existing student report from database
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
     * retrieves all student reports from database
     *
     * @return list of student reports
     */
    @Transactional
    public List<StudentReport> getAllStudentReports() {
        return ServiceUtils.toList(studentReportRepository.findAll());
    }
    /**
     * updates existing student report in database
     *
     * @param sr
     * @param status
     * @param c
     * @param sections
     * @return updated student report
     */
    @Transactional
    public StudentReport updateStudentReport(
            StudentReport sr, ReportStatus status, Coop c, List<ReportSection> sections) {
        StringBuilder error = new StringBuilder();
        if (sr == null) {
            error.append("Student Report cannot be null! ");
        }
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        sr.setStatus(status);
        sr.setCoop(c);
        if (sections != null) {
            sr.setReportSections(sections);
        }

        studentReportRepository.save(sr);

        // add/set employer report to coop
        boolean coopContains = false;

        List<StudentReport> coopReports = c.getStudentReports();
        for (StudentReport coopStudentReport : coopReports) {
            if (coopStudentReport.getId() == sr.getId()) {
                int index = coopReports.indexOf(coopStudentReport);
                coopReports.set(index, sr);
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
     * deletes specified student report from database
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
        List<StudentReport> coopReports = c.getStudentReports();
        coopReports.remove(sr);
        c.setStudentReports(coopReports);
        coopRepository.save(c);

        studentReportRepository.delete(sr);

        return sr;
    }
}
