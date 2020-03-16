package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.service.EmployerReportSectionService;
import ca.mcgill.cooperator.service.ReportConfigService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
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
public class ReportSectionConfigController {

    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;
    @Autowired StudentReportSectionService studentReportSectionService;
    @Autowired EmployerReportSectionService employerReportSectionService;

    /**
     * Returns the ReportSectionConfig with specified ID
     *
     * @param id
     * @return the ReportSectionConfig with specified ID
     */
    @GetMapping("/{id}")
    public ReportSectionConfigDto getReportConfigById(@PathVariable int id) {
        return ControllerUtils.convertToDto(reportSectionConfigService.getReportSectionConfig(id));
    }

    /**
     * Creates a new ReportSectionConfig
     *
     * @param rcDto
     * @return the created ReportSectionConfig
     */
    @PostMapping("")
    public ReportSectionConfigDto createReportSectionConfig(
            @RequestBody ReportSectionConfigDto rscDto) {
        ReportConfig reportConfig =
                reportConfigService.getReportConfig(rscDto.getReportConfig().getId());
        ReportSectionConfig reportSectionConfig =
                reportSectionConfigService.createReportSectionConfig(
                        rscDto.getSectionPrompt(), rscDto.getResponseType(), reportConfig);

        return ControllerUtils.convertToDto(reportSectionConfig);
    }

    /**
     * Updates an existing ReportSectionConfig
     *
     * @param rscDto
     * @return the updated ReportSectionConfig
     */
    @PutMapping("")
    public ReportSectionConfigDto updateReportSectionConfig(
            @RequestBody ReportSectionConfigDto rscDto) {
        ReportConfig reportConfig =
                reportConfigService.getReportConfig(rscDto.getReportConfig().getId());
        ReportSectionConfig rsConfig =
                reportSectionConfigService.getReportSectionConfig(rscDto.getId());

        ReportSectionConfig updatedReportSectionConfig =
                reportSectionConfigService.updateReportSectionConfig(
                        rsConfig,
                        rscDto.getSectionPrompt(),
                        rscDto.getResponseType(),
                        reportConfig,
                        ControllerUtils.convertEmployerReportSectionsToDomainObjects(
                                employerReportSectionService, rscDto.getEmployerReportSections()),
                        ControllerUtils.convertStudentReportSectionsToDomainObjects(
                                studentReportSectionService, rscDto.getStudentReportSections()));

        return ControllerUtils.convertToDto(updatedReportSectionConfig);
    }

    /**
     * Deletes an existing ReportSectionConfig
     *
     * @param id
     * @return the deleted ReportSectionConfig
     */
    @DeleteMapping("/{id")
    public ReportSectionConfigDto deleteReportSectionConfig(@PathVariable int id) {
        ReportSectionConfig rsConfig =
                reportSectionConfigService.deleteReportSectionConfig(
                        reportSectionConfigService.getReportSectionConfig(id));
        return ControllerUtils.convertToDto(rsConfig);
    }
}
