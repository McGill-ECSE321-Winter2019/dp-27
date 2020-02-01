package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.StudentReportService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@RequestMapping("student-reports")
public class StudentReportController {

    @Autowired StudentReportService studentReportService;
    @Autowired StudentService studentService;
    @Autowired CoopService coopService;

    /**
     * Get a Student Report by ID
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
     * Get Student Reports by Student ID
     *
     * @param id
     * @return StudentReportDto object
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
     * Create a Student Report using multipart form data
     *
     * @param file
     * @param status
     * @param title
     * @param coop_id
     * @return the created Student Report
     */
    @PostMapping("")
    public StudentReportDto createStudentReport(
            @ModelAttribute("file") MultipartFile file,
            @RequestParam("status") String status,
            @RequestParam("title") String title,
            @RequestParam("coop_id") int coopId) {
        Coop coop = coopService.getCoopById(coopId);
        ReportStatus reportStatus = ReportStatus.valueOf(status);

        StudentReport createdReport =
                studentReportService.createStudentReport(reportStatus, coop, title, file);

        return ControllerUtils.convertToDto(createdReport);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseEntity<Exception>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
