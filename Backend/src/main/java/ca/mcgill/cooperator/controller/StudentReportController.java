package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.dto.StudentReportSectionDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
import ca.mcgill.cooperator.service.StudentReportService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.HashSet;
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
@RequestMapping("student-reports")
public class StudentReportController extends BaseController {

    @Autowired StudentReportService studentReportService;
    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired StudentService studentService;
    @Autowired CoopService coopService;
    
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
     * Updates an existing StudentReport
     * 
     * @param id
     * 
     * In request body:
     *
     * @param reportId
     * @param file
     * @param status
     * @param title
     * @param coopId
     * @param rsDtos
     * @return the updated StudentReport
     */
    @PutMapping("/{id}")
    public StudentReportDto updateStudentReport(
            @PathVariable int id,
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("status") String status,
            @RequestParam("title") String title,
            @RequestParam("coop_id") int coopId,
            @RequestBody Set<StudentReportSectionDto> rsDtos) {
        StudentReport reportToUpdate = studentReportService.getStudentReport(id);

        Set<StudentReportSection> sections = null;
        if (rsDtos != null) {
            sections =
                    ControllerUtils.convertStudentReportSectionsToDomainObjects(
                            studentReportSectionService, rsDtos);
        }

        Coop coop = coopService.getCoopById(coopId);
        ReportStatus reportStatus = ReportStatus.valueOf(status);

        StudentReport updatedReport =
                studentReportService.updateStudentReport(
                        reportToUpdate, reportStatus, title, coop, sections, file);

        return ControllerUtils.convertToDto(updatedReport);
    }

    /**
     * Deletes an existing StudentReport
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
}
