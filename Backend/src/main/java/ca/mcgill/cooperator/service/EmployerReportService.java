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
     * Creates a new EmployerReport
     *
     * @param status
     * @param coop
     * @param title
     * @param employerContact
     * @param file
     * @return the created EmployerReport
     */
    @Transactional
    public EmployerReport createEmployerReport(
            ReportStatus status,
            Coop coop,
            String title,
            EmployerContact employerContact,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (coop == null) {
            error.append("Coop cannot be null! ");
        }
        if (employerContact == null) {
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
        er.setCoop(coop);
        er.setEmployerContact(employerContact);
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
     * Gets an existing EmployerReport by ID
     *
     * @param id
     * @return EmployerReport with specified ID
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
     * Gets all EmployerReports
     *
     * @return all EmployerReports
     */
    @Transactional
    public List<EmployerReport> getAllEmployerReports() {
        return ServiceUtils.toList(employerReportRepository.findAll());
    }

    /**
     * Updates an EmployerReport
     *
     * @param employerReport
     * @param status
     * @param coop
     * @param employerContact
     * @param sections
     * @return the updated EmployerReport
     */
    @Transactional
    public EmployerReport updateEmployerReport(
            EmployerReport employerReport,
            ReportStatus status,
            Coop coop,
            String title,
            EmployerContact employerContact,
            Set<EmployerReportSection> sections,
            MultipartFile file) {
        StringBuilder error = new StringBuilder();
        if (employerReport == null) {
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
            employerReport.setStatus(status);
        }
        if (title != null) {
            employerReport.setTitle(title);
        }
        if (coop != null) {
            employerReport.setCoop(coop);
        }
        if (employerContact != null) {
            employerReport.setEmployerContact(employerContact);
        }
        if (sections != null) {
            employerReport.setReportSections(sections);
        }
        try {
            employerReport.setData(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        employerReportRepository.save(employerReport);

        // need to update sections side since it doesn't sync
        if (sections != null) {
            // set employer report as parent for all report sections
            for (EmployerReportSection section : sections) {
                section.setEmployerReport(employerReport);
                employerReportSectionRepository.save(section);
            }
        }

        return employerReportRepository.save(employerReport);
    }

    /**
     * Deletes specific EmployerReport from database
     *
     * @param employerReport
     * @return the deleted EmployerReport
     */
    @Transactional
    public EmployerReport deleteEmployerReport(EmployerReport employerReport) {
        if (employerReport == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Employer Report to delete cannot be null!");
        }

        // first delete from all parents
        Coop c = employerReport.getCoop();
        // make new Set to avoid pointer issues
        Set<EmployerReport> coopReports = new HashSet<>(c.getEmployerReports());
        coopReports.remove(employerReport);
        c.setEmployerReports(coopReports);
        coopRepository.save(c);

        EmployerContact ec = employerReport.getEmployerContact();
        Set<EmployerReport> employerReports = new HashSet<>(ec.getEmployerReports());
        employerReports.remove(employerReport);
        ec.setEmployerReports(employerReports);
        employerContactRepository.save(ec);

        employerReportRepository.delete(employerReport);

        return employerReport;
    }
}
