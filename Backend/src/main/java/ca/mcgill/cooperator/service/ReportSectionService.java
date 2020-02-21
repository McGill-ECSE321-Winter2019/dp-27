package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.StudentReport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportSectionService {

    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired StudentReportRepository studentReportRepository;
    @Autowired EmployerReportRepository employerReportRepository;

    /**
     * creates new report section in the database
     *
     * @param title
     * @param content
     * @return created report section
     */
    @Transactional
    public ReportSection createReportSection(String title, String content) {
        StringBuilder error = new StringBuilder();
        if (title == null || title.trim().length() == 0) {
            error.append("Title cannot be empty! ");
        }
        if (content == null || content.trim().length() == 0) {
            error.append("Content cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        ReportSection rs = new ReportSection();
        rs.setTitle(title.trim());
        rs.setContent(content.trim());

        return reportSectionRepository.save(rs);
    }

    /**
     * retrieves specified existing report section from database
     *
     * @param id
     * @return specifed report section
     */
    @Transactional
    public ReportSection getReportSection(int id) {
        ReportSection rs = reportSectionRepository.findById(id).orElse(null);
        if (rs == null) {
            throw new IllegalArgumentException("Report Section with ID " + id + " does not exist!");
        }

        return rs;
    }

    /**
     * returns all report sections from the database
     *
     * @return list of report sections
     */
    @Transactional
    public List<ReportSection> getAllReportSections() {
        return ServiceUtils.toList(reportSectionRepository.findAll());
    }

    /**
     * updates existing report section in database
     *
     * @param rs
     * @param title
     * @param content
     * @param sr
     * @param er
     * @return updated report section
     */
    @Transactional
    public ReportSection updateReportSection(
            ReportSection rs, String title, String content, StudentReport sr, EmployerReport er) {
        StringBuilder error = new StringBuilder();
        if (rs == null) {
            error.append("Report Section cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("Title cannot be empty! ");
        }
        if (content != null && content.trim().length() == 0) {
            error.append("Content cannot be empty! ");
        }
        if (sr != null && er != null) {
            error.append("Cannot add to both Student Report and Employer Report!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        if (title != null && title.trim().length() > 0) {
            rs.setTitle(title.trim());
        }
        if (content != null && content.trim().length() > 0) {
            rs.setContent(content.trim());
        }

        if (sr != null) {
            rs.setStudentReport(sr);
            rs = reportSectionRepository.save(rs);
            boolean contains = false;
            Set<ReportSection> sections = new HashSet<ReportSection>();
            sections.addAll(sr.getReportSections());
            for (ReportSection section : sections) {
                if (section.getId() == rs.getId()) {
                    sections.remove(section);
                    sections.add(section);
                    contains = true;
                }
            }
            if (contains == false) {
                sections.add(rs);
            }
            sr.setReportSections(sections);
            studentReportRepository.save(sr);
        }

        if (er != null) {
            rs.setEmployerReport(er);
            rs = reportSectionRepository.save(rs);
            boolean contains = false;
            Set<ReportSection> sections = new HashSet<ReportSection>();
            sections.addAll(er.getReportSections());
            for (ReportSection section : sections) {
                if (section.getId() == rs.getId()) {
                    sections.remove(section);
                    sections.add(section);
                    contains = true;
                }
            }
            if (contains == false) {
                sections.add(rs);
            }
            er.setReportSections(sections);
            employerReportRepository.save(er);
        }

        return reportSectionRepository.save(rs);
    }

    /**
     * delete specified report section from database
     *
     * @param rs
     * @return deleted report section
     */
    @Transactional
    public ReportSection deleteReportSection(ReportSection rs) {
        if (rs == null) {
            throw new IllegalArgumentException("Report Section to delete cannot be null!");
        }

        // first delete from all parents
        StudentReport sr = rs.getStudentReport();
        if (sr != null) {
            Set<ReportSection> sections = sr.getReportSections();
            sections.remove(rs);
            sr.setReportSections(sections);
            studentReportRepository.save(sr);
        }

        EmployerReport er = rs.getEmployerReport();
        if (er != null) {
            Set<ReportSection> sections = er.getReportSections();
            sections.remove(rs);
            er.setReportSections(sections);
            employerReportRepository.save(er);
        }

        rs.setEmployerReport(null);
        rs.setStudentReport(null);
        reportSectionRepository.save(rs);

        reportSectionRepository.delete(rs);
        return rs;
    }
}
