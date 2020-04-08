package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import ca.mcgill.cooperator.service.ReportSectionConfigService;
import ca.mcgill.cooperator.service.ReportSectionService;
import ca.mcgill.cooperator.service.ReportService;
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
@RequestMapping("report-sections")
public class ReportSectionController extends BaseController {

    @Autowired ReportSectionService reportSectionService;
    @Autowired ReportService reportService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    @GetMapping("/{id}")
    public ReportSectionDto getReportSectionById(@PathVariable int id) {
        ReportSection reportSection = reportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(reportSection);
    }

    @GetMapping("")
    public List<ReportSectionDto> getAllReportSections() {
        List<ReportSection> reportSections = reportSectionService.getAllReportSections();
        return ControllerUtils.convertReportSectionListToDto(reportSections);
    }

    @PostMapping("")
    public ReportSectionDto createReportSection(@RequestBody ReportSectionDto reportSectionDto) {
        ReportSectionConfig reportSectionConfig = null;
        Report report = null;

        if (reportSectionDto.getReportSectionConfig() != null) {
            ReportSectionConfigDto rsConfigDto = reportSectionDto.getReportSectionConfig();
            reportSectionConfig =
                    reportSectionConfigService.getReportSectionConfig(rsConfigDto.getId());
        }
        if (reportSectionDto.getReport() != null) {
            ReportDto reportDto = reportSectionDto.getReport();
            report = reportService.getReport(reportDto.getId());
        }

        ReportSection reportSection =
                reportSectionService.createReportSection(
                        reportSectionDto.getResponse(), reportSectionConfig, report);
        return ControllerUtils.convertToDto(reportSection);
    }

    @PutMapping("/{id}")
    public ReportSectionDto updateReportSection(
            @PathVariable int id, @RequestBody ReportSectionDto reportSectionDto) {
        ReportSection reportSection = reportSectionService.getReportSection(id);

        ReportSectionConfig reportSectionConfig = null;
        Report report = null;

        if (reportSectionDto.getReportSectionConfig() != null) {
            ReportSectionConfigDto rsConfigDto = reportSectionDto.getReportSectionConfig();
            reportSectionConfig =
                    reportSectionConfigService.getReportSectionConfig(rsConfigDto.getId());
        }
        if (reportSectionDto.getReport() != null) {
            ReportDto reportDto = reportSectionDto.getReport();
            report = reportService.getReport(reportDto.getId());
        }

        reportSection =
                reportSectionService.updateReportSection(
                        reportSection, reportSectionDto.getResponse(), reportSectionConfig, report);

        return ControllerUtils.convertToDto(reportSection);
    }

    @DeleteMapping("/{id}")
    public ReportSectionDto deleteReportSection(@PathVariable int id) {
        ReportSection reportSection = reportSectionService.getReportSection(id);
        return ControllerUtils.convertToDto(
                reportSectionService.deleteReportSection(reportSection));
    }
}
