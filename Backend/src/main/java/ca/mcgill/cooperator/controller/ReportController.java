package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.model.Author;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.service.AuthorService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.ReportSectionService;
import ca.mcgill.cooperator.service.ReportService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("reports")
public class ReportController extends BaseController {
    @Autowired CoopService coopService;
    @Autowired AuthorService authorService;
    @Autowired ReportService reportService;
    @Autowired ReportSectionService reportSectionService;

    /**
     * Creates a Report using multipart form data
     *
     * @param file
     * @param status
     * @param title
     * @param coopId
     * @param authorId
     * @return the created Report
     */
    @PostMapping("")
    public ReportDto createReport(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam String status,
            @RequestParam String title,
            @RequestParam Integer coopId,
            @RequestParam Integer authorId) {
        Coop coop = coopService.getCoopById(coopId);

        Author a = authorService.getAuthorById(authorId);
        ReportStatus reportStatus = ReportStatus.valueOf(status);

        Report createdReport = reportService.createReport(reportStatus, coop, title, a, file);

        return ControllerUtils.convertToDto(createdReport);
    }

    /**
     * Gets a Report by ID
     *
     * @param id
     * @return ReportDto
     */
    @GetMapping("/{id}")
    public ReportDto getReportById(@PathVariable int id) {
        Report r = reportService.getReport(id);

        return ControllerUtils.convertToDto(r);
    }

    @GetMapping("")
    public List<ReportDto> getAllReports() {
        return ControllerUtils.convertReportListToDto(reportService.getAllReports());
    }

    /**
     * Gets all Reports associated with the specified Author
     *
     * @param id
     * @return list of ReportDto
     */
    @GetMapping("/author/{id}")
    public List<ReportDto> getReportByAuthorId(@PathVariable int id) {
        Author a = authorService.getAuthorById(id);

        return ControllerUtils.convertReportListToDto(a.getReports());
    }

    /**
     * Updates an existing Report
     *
     * @param id
     * @param file
     * @param status
     * @param title
     * @param coopId
     * @param rsDtos
     * @param authorId
     * @return updated Report
     */
    @PutMapping("/{id}")
    public ReportDto updateReport(
            @PathVariable int id,
            @ModelAttribute("file") MultipartFile file,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer coopId,
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Set<ReportSectionDto> reportSections) {
        Report reportToUpdate = reportService.getReport(id);

        Coop coop = null; 
        if (coopId != null) {
        	coop = coopService.getCoopById(coopId);
        }
        
        Author author = null;
        if (authorId != null) {
        	author = authorService.getAuthorById(authorId);
        }
        
        ReportStatus reportStatus = null; 
        if (status != null) {
        	reportStatus = ReportStatus.valueOf(status);
        }
        
        Set<ReportSection> sections = null;
        if (reportSections != null) {
            sections =
                    ControllerUtils.convertReportSectionsToDomainObjects(
                            reportSectionService, reportSections);
        }

        Report updatedReport =
                reportService.updateReport(
                        reportToUpdate, reportStatus, coop, title, author, sections, file);

        return ControllerUtils.convertToDto(updatedReport);
    }

    /**
     * Deletes a Report
     *
     * @param id
     * @return the deleted Report
     */
    @DeleteMapping("/{id}")
    public ReportDto deleteReport(@PathVariable int id) {
        Report report = reportService.getReport(id);
        report = reportService.deleteReport(report);

        return ControllerUtils.convertToDto(report);
    }
}
