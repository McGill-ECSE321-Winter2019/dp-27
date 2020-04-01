package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.dto.StudentReportSectionDto;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.model.StudentReportSection;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
import ca.mcgill.cooperator.service.StudentReportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("student-report-sections")
public class StudentReportSectionController extends BaseController {

    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired StudentReportService studentReportService;
    @Autowired ReportSectionConfigService reportSectionConfigService;
    
    /**
     * Creates a new StudentReportSection
     * 
     * In request body:
     * 
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return the created StudentReportSection
     */
    @PostMapping("")
    public StudentReportSectionDto createReportSection(
            @RequestBody StudentReportSectionDto reportSectionDto) {
        ReportSectionConfig rsConfig = null;
        if (reportSectionDto.getReportSectionConfig() != null) {
            rsConfig =
                    reportSectionConfigService.getReportSectionConfig(
                            reportSectionDto.getReportSectionConfig().getId());
        }
        StudentReport sr = null;
        if (reportSectionDto.getStudentReport() != null) {
            sr = studentReportService.getStudentReport(reportSectionDto.getStudentReport().getId());
        }

        StudentReportSection reportSection =
                studentReportSectionService.createReportSection(
                        reportSectionDto.getResponse(), rsConfig, sr);
        return ControllerUtils.convertToDto(reportSection);
    }

    /**
     * Gets a StudentReportSection by ID
     * 
     * @param id
     * @return StudentReportSectionDto
     */
    @GetMapping("/{id}")
    public StudentReportSectionDto getReportSectionById(@PathVariable int id) {
        StudentReportSection reportSection = studentReportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(reportSection);
    }

    /**
     * Gets all StudentReportSections
     * 
     * @return list of StudentReportSectionDtos
     */
    @GetMapping("")
    public List<StudentReportSectionDto> getAllReportSections() {
        List<StudentReportSection> reportSections =
                studentReportSectionService.getAllReportSections();
        return ControllerUtils.convertStudentReportSectionListToDto(reportSections);
    }

    /**
     * Updates an existing StudentReportSection
     * 
     * @param id
     * 
     * In request body:
     * 
     * @param response
     * @param reportSectionConfig
     * @param studentReport
     * @return the updated StudentReportSection
     */
    @PutMapping("/{id}")
    public StudentReportSectionDto updateReportSection(
            @PathVariable int id, @RequestBody StudentReportSectionDto reportSectionDto) {
        StudentReportSection reportSection = studentReportSectionService.getReportSection(id);
        ReportSectionConfig reportSectionConfig = null;
        StudentReport studentReport = null;

        if (reportSectionDto.getReportSectionConfig() != null) {
            ReportSectionConfigDto rsConfigDto = reportSectionDto.getReportSectionConfig();
            reportSectionConfig =
                    reportSectionConfigService.getReportSectionConfig(rsConfigDto.getId());
        }
        if (reportSectionDto.getStudentReport() != null) {
            StudentReportDto studentReportDto = reportSectionDto.getStudentReport();
            studentReport = studentReportService.getStudentReport(studentReportDto.getId());
        }

        reportSection =
                studentReportSectionService.updateReportSection(
                        reportSection,
                        reportSectionDto.getResponse(),
                        reportSectionConfig,
                        studentReport);

        return ControllerUtils.convertToDto(reportSection);
    }

    /**
     * Deletes an existing StudentReportSection
     * 
     * @param id
     * @return the deleted StudentReportSection
     */
    @DeleteMapping("/{id}")
    public StudentReportSectionDto deleteReportSection(@PathVariable int id) {
        StudentReportSection reportSection = studentReportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(
                studentReportSectionService.deleteReportSection(reportSection));
    }
}
