package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerReportService {
    @Autowired EmployerReportRepository employerReportRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired ReportSectionRepository reportSectionRepository;

    /**
     * creates new employer report in database
     *
     * @param status
     * @param c
     * @param ec
     * @return created employer report
     */
    @Transactional
    public EmployerReport createEmployerReport(ReportStatus status, Coop c, EmployerContact ec) {
        StringBuilder error = new StringBuilder();
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null! ");
        }
        if (ec == null) {
            error.append("Employer Contact cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        EmployerReport er = new EmployerReport();
        er.setStatus(status);
        er.setCoop(c);
        er.setEmployerContact(ec);
        er.setReportSections(new ArrayList<ReportSection>());

        employerReportRepository.save(er);

        Set<EmployerReport> coopReports = c.getEmployerReports();
        coopReports.add(er);
        c.setEmployerReports(coopReports);

        Set<EmployerReport> ecReports = ec.getEmployerReports();
        ecReports.add(er);
        ec.setEmployerReports(ecReports);

        coopRepository.save(c);
        employerContactRepository.save(ec);

        return employerReportRepository.save(er);
    }

    /**
     * retrieves specified existing employer report from database
     *
     * @param id
     * @return specific employer report
     */
    @Transactional
    public EmployerReport getEmployerReport(int id) {
        EmployerReport er = employerReportRepository.findById(id).orElse(null);
        if (er == null) {
            throw new IllegalArgumentException(
                    "Employer Report with ID " + id + " does not exist!");
        }

        return er;
    }

    /**
     * retrieves all employer reports from database
     *
     * @return list of empoyer reports
     */
    @Transactional
    public List<EmployerReport> getAllEmployerReports() {
        return ServiceUtils.toList(employerReportRepository.findAll());
    }

    /**
     * update existing employer report in database
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
            EmployerContact ec,
            List<ReportSection> sections) {
        StringBuilder error = new StringBuilder();
        if (er == null) {
            error.append("Employer Report cannot be null! ");
        }
        if (status == null) {
            error.append("Report Status cannot be null! ");
        }
        if (c == null) {
            error.append("Coop cannot be null! ");
        }
        if (ec == null) {
            error.append("Employer Contact cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        // set all values in employer report
        er.setStatus(status);
        er.setCoop(c);
        er.setEmployerContact(ec);
        if (sections != null) {
            er.setReportSections(sections);
        }

        employerReportRepository.save(er);

        // add/set employer report to coop
        boolean coopContains = false;

        Set<EmployerReport> coopReports = c.getEmployerReports();
        for (EmployerReport coopEmployerReport : coopReports) {
            if (coopEmployerReport.getId() == er.getId()) {
                coopReports.remove(coopEmployerReport);
                coopReports.add(er);
                coopContains = true;
            }
        }

        if (coopContains == false) {
            coopReports.add(er);
        }
        c.setEmployerReports(coopReports);

        coopRepository.save(c);

        // add/set employer report to employer contact
        boolean employerContains = false;

        Set<EmployerReport> employerReports = c.getEmployerReports();
        for (EmployerReport employerReport : employerReports) {
            if (employerReport.getId() == er.getId()) {
                employerReports.remove(employerReport);
                employerReports.add(er);
                employerContains = true;
            }
        }

        if (employerContains == false) {
            employerReports.add(er);
        }
        ec.setEmployerReports(employerReports);

        employerContactRepository.save(ec);

        // set employer report as parent for all report sections
        for (ReportSection section : sections) {
            section.setEmployerReport(er);
            reportSectionRepository.save(section);
        }

        return employerReportRepository.save(er);
    }

    /**
     * deletes specific employer report from database
     *
     * @param er
     * @return deleted employer report
     */
    @Transactional
    public EmployerReport deleteEmployerReport(EmployerReport er) {
        if (er == null) {
            throw new IllegalArgumentException("Employer Report to delete cannot be null!");
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
