package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.service.ReportConfigService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("report-configs")
public class ReportConfigController extends BaseController {

    @Autowired ReportConfigService reportConfigService;

    /**
     * Returns ReportConfig with specified ID
     *
     * @param id
     * @return ReportConfig with specified ID
     */
    @GetMapping("/{id}")
    public ReportConfigDto getReportConfigById(@PathVariable int id) {
        return ControllerUtils.convertToDto(reportConfigService.getReportConfig(id));
    }

    @GetMapping("")
    public List<ReportConfigDto> getAllReportConfigs() {
        return ControllerUtils.convertReportConfigListToDto(
                reportConfigService.getAllReportConfigs());
    }

    /**
     * Creates a new ReportConfig
     *
     * @param rcDto
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
}
