package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.model.StudentReportSection;
import ca.mcgill.cooperator.service.EmployerReportSectionService;
import ca.mcgill.cooperator.service.ReportConfigService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
import java.util.List;
import java.util.Set;
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
@RequestMapping("report-section-configs")
public class ReportSectionConfigController extends BaseController {

    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;
    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired EmployerReportSectionService employerReportSectionService;

    /**
     * Creates a new ReportSectionConfig
     *
     * <p>In request body:
     *
     * @param sectionPrompt
     * @param responseType
     * @param questionNumber
     * @param reportConfig
     * @return the created ReportSectionConfig
     */
    @PostMapping("")
    public ReportSectionConfigDto createReportSectionConfig(
            @RequestBody ReportSectionConfigDto rscDto) {
        ReportConfig reportConfig = null;
        if (rscDto.getReportConfig() != null) {
            reportConfig = reportConfigService.getReportConfig(rscDto.getReportConfig().getId());
        }

        ReportSectionConfig reportSectionConfig =
                reportSectionConfigService.createReportSectionConfig(
                        rscDto.getSectionPrompt(),
                        rscDto.getResponseType(),
                        rscDto.getQuestionNumber(),
                        reportConfig);

        return ControllerUtils.convertToDto(reportSectionConfig);
    }

    /**
     * Gets the ReportSectionConfig with specified ID
     *
     * @param id
     * @return ReportSectionConfigDto object
     */
    @GetMapping("/{id}")
    public ReportSectionConfigDto getReportSectionConfigById(@PathVariable int id) {
        return ControllerUtils.convertToDto(reportSectionConfigService.getReportSectionConfig(id));
    }

    /**
     * Gets all ReportSectionConfigs
     *
     * @return list of ReportSectionConfigDtos
     */
    @GetMapping("")
    public List<ReportSectionConfigDto> getAllReportSectionConfigs() {
        return ControllerUtils.convertReportSectionConfigListToDto(
                reportSectionConfigService.getAllReportSectionConfigs());
    }

    /**
     * Gets all ReportSectionConfig response types
     *
     * @return list of all the response types
     */
    @GetMapping("/response-types")
    public List<String> getReportSectionConfigResponseTypes() {
        return reportSectionConfigService.getAllResponseTypes();
    }

    /**
     * Updates an existing ReportSectionConfig
     *
     * @param id
     *     <p>In request body:
     * @param sectionPrompt
     * @param responseType
     * @param questionNumber
     * @param reportConfig
     * @param employerReportSections
     * @param studentReportSections
     * @return the updated ReportSectionConfig
     */
    @PutMapping("/{id}")
    public ReportSectionConfigDto updateReportSectionConfig(
            @PathVariable int id, @RequestBody ReportSectionConfigDto rscDto) {
        ReportConfig reportConfig = null;
        if (rscDto.getReportConfig() != null) {
            reportConfig = reportConfigService.getReportConfig(rscDto.getReportConfig().getId());
        }

        Set<EmployerReportSection> employerReportSections = null;
        if (rscDto.getEmployerReportSections() != null) {
            employerReportSections =
                    ControllerUtils.convertEmployerReportSectionsToDomainObjects(
                            employerReportSectionService, rscDto.getEmployerReportSections());
        }

        Set<StudentReportSection> studentReportSections = null;
        if (rscDto.getStudentReportSections() != null) {
            studentReportSections =
                    ControllerUtils.convertStudentReportSectionsToDomainObjects(
                            studentReportSectionService, rscDto.getStudentReportSections());
        }

        ReportSectionConfig rsConfig = reportSectionConfigService.getReportSectionConfig(id);

        ReportSectionConfig updatedReportSectionConfig =
                reportSectionConfigService.updateReportSectionConfig(
                        rsConfig,
                        rscDto.getSectionPrompt(),
                        rscDto.getResponseType(),
                        rscDto.getQuestionNumber(),
                        reportConfig,
                        employerReportSections,
                        studentReportSections);

        return ControllerUtils.convertToDto(updatedReportSectionConfig);
    }

    /**
     * Deletes an existing ReportSectionConfig
     *
     * @param id
     * @return the deleted ReportSectionConfig
     */
    @DeleteMapping("/{id}")
    public ReportSectionConfigDto deleteReportSectionConfig(@PathVariable int id) {
        ReportSectionConfig rsConfig =
                reportSectionConfigService.deleteReportSectionConfig(
                        reportSectionConfigService.getReportSectionConfig(id));
        return ControllerUtils.convertToDto(rsConfig);
    }
}
