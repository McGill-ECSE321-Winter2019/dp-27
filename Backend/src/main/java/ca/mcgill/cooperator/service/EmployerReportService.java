package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.EmployerReportSectionRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
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
public class EmployerReportService extends BaseService {

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired EmployerReportSectionRepository employerReportSectionRepository;
    @Autowired CoopRepository coopRepository;

    /**
     * Creates new employer report in database
     *
     * @param status
     * @param c
     * @param title
     * @param ec
     * @param file
     * @return created employer report
     */
    @Transactional
    public EmployerReport createEmployerReport(
            ReportStatus status, Coop c, String title, EmployerContact ec, MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null! ");
        }
        if (ec == null) {
            error.append("Employer Contact cannot be null! ");
        }
        if (title == null || title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        EmployerReport er = new EmployerReport();
        er.setStatus(status);
        er.setTitle(title);
        er.setCoop(c);
        er.setEmployerContact(ec);
        er.setReportSections(new HashSet<EmployerReportSection>());
        if (file != null) {
            try {
                er.setData(file.getBytes());
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return employerReportRepository.save(er);
    }

    /**
     * Retrieves specified existing employer report from database
     *
     * @param id
     * @return specific employer report
     */
    @Transactional
    public EmployerReport getEmployerReport(int id) {
        EmployerReport er = employerReportRepository.findById(id).orElse(null);
        if (er == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer Report with ID " + id + " does not exist!");
        }

        return er;
    }

    /**
     * Retrieves all employer reports from database
     *
     * @return list of employer reports
     */
    @Transactional
    public List<EmployerReport> getAllEmployerReports() {
        return ServiceUtils.toList(employerReportRepository.findAll());
    }

    /**
     * Updates existing employer report in database
     *
     * @param er
     * @param status
     * @param c
     * @param ec
     * @param sections
     * @return updated employer report
     */
    @Transactional
    public EmployerReport updateEmployerReport(
            EmployerReport er,
            ReportStatus status,
            Coop c,
            String title,
            EmployerContact ec,
            Set<EmployerReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (er == null) {
            error.append("Employer Report cannot be null! ");
        }
        if (title != null && title.trim().length() == 0) {
            error.append("File title cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        // set all values in employer report if they're not null
        if (status != null) {
            er.setStatus(status);
        }
        if (title != null) {
            er.setTitle(title);
        }
        if (c != null) {
            er.setCoop(c);
        }
        if (ec != null) {
            er.setEmployerContact(ec);
        }
        if (sections != null) {
            er.setReportSections(sections);
        }
        try {
            er.setData(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        employerReportRepository.save(er);

        // need to update sections side since it doesn't sync
        if (sections != null) {
            // set employer report as parent for all report sections
            for (EmployerReportSection section : sections) {
                section.setEmployerReport(er);
                employerReportSectionRepository.save(section);
            }
        }

        return employerReportRepository.save(er);
    }

    /**
     * Deletes specific employer report from database
     *
     * @param er
     * @return deleted employer report
     */
    @Transactional
    public EmployerReport deleteEmployerReport(EmployerReport er) {
        if (er == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer Report to delete cannot be null!");
        }

        // first delete from all parents
        Coop c = er.getCoop();
        Set<EmployerReport> coopReports = c.getEmployerReports();
        coopReports.remove(er);
        c.setEmployerReports(coopReports);
        coopRepository.save(c);

        EmployerContact ec = er.getEmployerContact();
        Set<EmployerReport> employerReports = ec.getEmployerReports();
        employerReports.remove(er);
        ec.setEmployerReports(employerReports);
        employerContactRepository.save(ec);

        employerReportRepository.delete(er);

        return er;
    }
}
