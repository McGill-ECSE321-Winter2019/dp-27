package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.ReportSectionService;
import ca.mcgill.cooperator.service.StudentReportService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping("student-reports")
public class StudentReportController {

    @Autowired StudentReportService studentReportService;
    @Autowired StudentService studentService;
    @Autowired CoopService coopService;
    @Autowired ReportSectionService reportSectionService;

    /**
     * Gets a StudentReport by ID
     *
     * @param id
     * @return StudentReportDto object
     */
    @GetMapping("/{id}")
    public StudentReportDto getStudentReportById(@PathVariable int id) {
        StudentReport sr = studentReportService.getStudentReport(id);

        return ControllerUtils.convertToDto(sr);
    }

    /**
     * Gets all StudentReports
     *
     * @return all StudentReports
     */
    @GetMapping("")
    public List<StudentReportDto> getAllStudentReports() {
        Set<StudentReport> reports = new HashSet<>(studentReportService.getAllStudentReports());

        return ControllerUtils.convertStudentReportListToDto(reports);
    }

    /**
     * Gets StudentReports by Student ID
     *
     * @param id
     * @return list of StudentReportDtos
     */
    @GetMapping("/student/{id}")
    public List<StudentReportDto> getStudentReportsByStudentId(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        Set<StudentReport> reports = new HashSet<>();

        for (Coop c : student.getCoops()) {
            reports.addAll(c.getStudentReports());
        }

        return ControllerUtils.convertStudentReportListToDto(reports);
    }

    /**
     * Creates a StudentReport using multipart form data
     *
     * @param file
     * @param status
     * @param title
     * @param coop_id
     * @return the created StudentReport
     */
    @PostMapping("")
    public StudentReportDto createStudentReport(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("status") String status,
            @RequestParam("title") String title,
            @RequestParam("coop_id") int coopId) {
        ReportStatus reportStatus = ReportStatus.valueOf(status);
        Coop coop = coopService.getCoopById(coopId);
        StudentReport createdReport =
                studentReportService.createStudentReport(reportStatus, coop, title, file);

        return ControllerUtils.convertToDto(createdReport);
    }

    /**
     * Updates a StudentReport, put not the actual contents of report
     *
     * @param reportId
     * @param status
     * @param title
     * @param rsDtos
     * @param coopId
     * @return the updated StudentReport
     */
    @PutMapping("/{id}")
    public StudentReportDto updateStudentReport(
            @PathVariable int id,
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("status") String status,
            @RequestParam("title") String title,
            @RequestParam("coop_id") int coopId,
            @RequestBody Set<ReportSectionDto> rsDtos) {
        StudentReport reportToUpdate = studentReportService.getStudentReport(id);

        Set<ReportSection> sections = convertReportSectionSetToDomainObject(rsDtos);

        Coop coop = coopService.getCoopById(coopId);
        ReportStatus reportStatus = ReportStatus.valueOf(status);

        StudentReport updatedReport =
                studentReportService.updateStudentReport(
                        reportToUpdate, reportStatus, title, coop, sections, file);

        return ControllerUtils.convertToDto(updatedReport);
    }

    /**
     * Deletes a StudentReport
     *
     * @param id
     * @return the deleted StudentReport
     */
    @DeleteMapping("/{id}")
    public StudentReportDto deleteStudentReport(@PathVariable int id) {
        StudentReport report = studentReportService.getStudentReport(id);
        report = studentReportService.deleteStudentReport(report);

        return ControllerUtils.convertToDto(report);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Set<ReportSection> convertReportSectionSetToDomainObject(Set<ReportSectionDto> rsDtos) {
        Set<ReportSection> reports = new HashSet<ReportSection>();
        for (ReportSectionDto rsDto : rsDtos) {
            ReportSection rs = reportSectionService.getReportSection(rsDto.getId());
            reports.add(rs);
        }
        return reports;
    }
}
