package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.service.ReportConfigService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import java.util.HashSet;
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
@RequestMapping("report-configs")
public class ReportConfigController extends BaseController {

    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    /**
     * Creates a new ReportConfig
     *
     * <p>In request body:
     *
     * @param requiresFile
     * @param deadline
     * @param isDeadlineFromStart
     * @param type
     * @return the created ReportConfig
     */
    @PostMapping("")
    public ReportConfigDto createReportConfig(@RequestBody ReportConfigDto rcDto) {
        ReportConfig reportConfig =
                reportConfigService.createReportConfig(
                        rcDto.getRequiresFile(),
                        rcDto.getDeadline(),
                        rcDto.getIsDeadlineFromStart(),
                        rcDto.getType());

        return ControllerUtils.convertToDto(reportConfig);
    }

    /**
     * Gets the ReportConfig with specified ID
     *
     * @param id
     * @return the ReportConfig with specified ID
     */
    @GetMapping("/{id}")
    public ReportConfigDto getReportConfigById(@PathVariable int id) {
        return ControllerUtils.convertToDto(reportConfigService.getReportConfig(id));
    }

    /**
     * Gets all ReportConfigs
     *
     * @return list of ReportConfigDtos
     */
    @GetMapping("")
    public List<ReportConfigDto> getAllReportConfigs() {
        return ControllerUtils.convertReportConfigListToDto(
                reportConfigService.getAllReportConfigs());
    }

    /**
     * Updates an existing ReportConfig
     *
     * @param id
     *     <p>In request body:
     * @param requiresFile
     * @param deadline
     * @param isDeadlineFromStart
     * @param type
     * @param reportSectionConfigs
     * @return the updated ReportConfig
     */
    @PutMapping("/{id}")
    public ReportConfigDto updateReportConfig(
            @PathVariable int id, @RequestBody ReportConfigDto rcDto) {
        ReportConfig rc = reportConfigService.getReportConfig(id);

        Set<ReportSectionConfig> rscConfigs = null;
        if (rcDto.getReportSectionConfigs() != null) {
            rscConfigs =
                    new HashSet<>(
                            ControllerUtils.convertReportSectionConfigDtosToDomainObjects(
                                    reportSectionConfigService, rcDto.getReportSectionConfigs()));
        }

        ReportConfig updatedReportConfig =
                reportConfigService.updateReportConfig(
                        rc,
                        rcDto.getRequiresFile(),
                        rcDto.getDeadline(),
                        rcDto.getIsDeadlineFromStart(),
                        rcDto.getType(),
                        rscConfigs);

        return ControllerUtils.convertToDto(updatedReportConfig);
    }

    /**
     * Deletes an existing ReportConfig
     *
     * @param id
     * @return the deleted ReportConfig
     */
    @DeleteMapping("/{id}")
    public ReportConfigDto deleteReportConfig(@PathVariable int id) {
        ReportConfig reportConfig =
                reportConfigService.deleteReportConfig(reportConfigService.getReportConfig(id));
        return ControllerUtils.convertToDto(reportConfig);
    }
}
