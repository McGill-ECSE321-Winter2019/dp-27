package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.EmployerReportSectionDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.EmployerReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.service.EmployerReportSectionService;
import ca.mcgill.cooperator.service.EmployerReportService;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
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
@RequestMapping("employer-report-sections")
public class EmployerReportSectionController extends BaseController {

    @Autowired EmployerReportSectionService employerReportSectionService;
    @Autowired EmployerReportService employerReportService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    @GetMapping("/{id}")
    public EmployerReportSectionDto getReportSectionById(@PathVariable int id) {
        EmployerReportSection reportSection = employerReportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(reportSection);
    }

    @GetMapping("")
    public List<EmployerReportSectionDto> getAllReportSections() {
        List<EmployerReportSection> reportSections =
                employerReportSectionService.getAllReportSections();
        return ControllerUtils.convertEmployerReportSectionListToDto(reportSections);
    }

    @PostMapping("")
    public EmployerReportSectionDto createReportSection(
            @RequestBody EmployerReportSectionDto reportSectionDto) {
        ReportSectionConfig rsConfig =
                reportSectionConfigService.getReportSectionConfig(
                        reportSectionDto.getReportSectionConfig().getId());
        EmployerReport er =
                employerReportService.getEmployerReport(
                        reportSectionDto.getEmployerReport().getId());

        EmployerReportSection reportSection =
                employerReportSectionService.createReportSection(
                        reportSectionDto.getResponse(), rsConfig, er);
        return ControllerUtils.convertToDto(reportSection);
    }

    @PutMapping("")
    public EmployerReportSectionDto updateReportSection(
            @RequestBody EmployerReportSectionDto reportSectionDto) {
        EmployerReportSection reportSection =
                employerReportSectionService.getReportSection(reportSectionDto.getId());
        ReportSectionConfig reportSectionConfig = null;
        EmployerReport employerReport = null;

        if (reportSectionDto.getReportSectionConfig() != null) {
            ReportSectionConfigDto rsConfigDto = reportSectionDto.getReportSectionConfig();
            reportSectionConfig =
                    reportSectionConfigService.getReportSectionConfig(rsConfigDto.getId());
        }
        if (reportSectionDto.getEmployerReport() != null) {
            EmployerReportDto employerReportDto = reportSectionDto.getEmployerReport();
            employerReport = employerReportService.getEmployerReport(employerReportDto.getId());
        }

        reportSection =
                employerReportSectionService.updateReportSection(
                        reportSection,
                        reportSectionDto.getResponse(),
                        reportSectionConfig,
                        employerReport);

        return ControllerUtils.convertToDto(reportSection);
    }

    @DeleteMapping("/{id}")
    public EmployerReportSectionDto deleteReportSection(@PathVariable int id) {
        EmployerReportSection reportSection = employerReportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(
                employerReportSectionService.deleteReportSection(reportSection));
    }
}
