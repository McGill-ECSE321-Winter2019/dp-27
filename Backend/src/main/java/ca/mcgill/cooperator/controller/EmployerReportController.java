package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.EmployerReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("employer-reports")
public class EmployerReportController {

    @Autowired CoopService coopService;
    @Autowired EmployerContactService employerContactService;
    @Autowired EmployerReportService employerReportService;

    /**
     * Gets an EmployerReport by ID
     *
     * @param id
     * @return EmployerReportDto
     */
    @GetMapping("/{id}")
    public EmployerReportDto getEmployerReportById(@PathVariable int id) {
        EmployerReport er = employerReportService.getEmployerReport(id);

        return ControllerUtils.convertToDto(er);
    }

    /**
     * Gets all EmployerReports associated with the specified EmployerContact
     *
     * @param id
     * @return list of EmployerReportDto
     */
    @GetMapping("/employer/{id}")
    public List<EmployerReportDto> getEmployerReportByEmployerContactId(@PathVariable int id) {
        EmployerContact ec = employerContactService.getEmployerContact(id);

        return ControllerUtils.convertEmployerReportListToDto(ec.getEmployerReports());
    }

    /**
     * Creates a EmployerReport using multipart form data
     *
     * @param file
     * @param status
     * @param title
     * @param coop_id
     * @param employer_id
     * @return the created Student Report
     */
    @PostMapping("")
    public EmployerReportDto createEmployerReport(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("status") String status,
            @RequestParam("title") String title,
            @RequestParam("coop_id") int coopId,
            @RequestParam("employer_id") int employerId) {
        Coop coop = coopService.getCoopById(coopId);
        EmployerContact ec = employerContactService.getEmployerContact(employerId);
        ReportStatus reportStatus = ReportStatus.valueOf(status);

        EmployerReport createdReport =
                employerReportService.createEmployerReport(reportStatus, coop, title, ec, file);

        return ControllerUtils.convertToDto(createdReport);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
