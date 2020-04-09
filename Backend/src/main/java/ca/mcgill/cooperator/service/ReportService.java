package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.model.Author;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReportService extends BaseService {

    @Autowired AuthorRepository authorRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired ReportSectionRepository reportSectionRepository;
    @Autowired CoopRepository coopRepository;

    /**
     * Creates new report in database
     *
     * @param status
     * @param c
     * @param title
     * @param a
     * @param file
     * @return
     */
    @Transactional
    public Report createReport(
            ReportStatus status, Coop coop, String title, Author author, MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (coop == null) {
            error.append("Coop cannot be null! ");
        }
        if (author == null) {
            error.append("Author cannot be null! ");
        }
        if (title == null || title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Report r = new Report();
        r.setStatus(status);
        r.setTitle(title);
        r.setCoop(coop);
        r.setAuthor(author);
        r.setReportSections(new HashSet<ReportSection>());

        if (file != null) {
            try {
                r.setData(file.getBytes());
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return reportRepository.save(r);
    }

    /**
     * Retrieves specified existing report from database
     *
     * @param id
     * @return specific report
     */
    @Transactional
    public Report getReport(int id) {
        Report r = reportRepository.findById(id).orElse(null);
        if (r == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Report with ID " + id + " does not exist!");
        }

        return r;
    }

    /**
     * Retrieves all reports from database
     *
     * @return list of reports
     */
    @Transactional
    public List<Report> getAllReports() {
        return ServiceUtils.toList(reportRepository.findAll());
    }

    /**
     * Updates existing report in database
     *
     * @param r
     * @param status
     * @param c
     * @param a
     * @param sections
     * @return updated report
     */
    @Transactional
    public Report updateReport(
            Report report,
            ReportStatus status,
            Coop coop,
            String title,
            Author author,
            Set<ReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (report == null) {
            error.append("Report cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        // set all values in employer report if they're not null
        if (status != null) {
            report.setStatus(status);
        }
        if (title != null) {
            report.setTitle(title);
        }
        if (coop != null) {
            report.setCoop(coop);
        }
        if (author != null) {
            report.setAuthor(author);
        }
        if (sections != null) {
            report.setReportSections(sections);
        }
        try {
            report.setData(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return reportRepository.save(report);
    }

    /**
     * Deletes specific report from database
     *
     * @param r
     * @return deleted report
     */
    @Transactional
    public Report deleteReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Report to delete cannot be null!");
        }

        // first delete from all parents
        Coop c = report.getCoop();
        Set<Report> coopReports = c.getReports();
        coopReports.remove(report);
        c.setReports(coopReports);
        coopRepository.save(c);

        Author a = report.getAuthor();
        Set<Report> reports = a.getReports();
        reports.remove(report);
        a.setReports(reports);
        authorRepository.save(a);

        reportRepository.delete(report);

        return report;
    }
}
