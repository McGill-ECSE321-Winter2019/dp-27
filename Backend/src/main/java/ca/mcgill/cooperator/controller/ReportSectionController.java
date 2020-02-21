package ca.mcgill.cooperator.controller;

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

import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.service.ReportSectionService;
import ca.mcgill.cooperator.service.StudentReportService;
import ca.mcgill.cooperator.service.EmployerReportService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("report-sections")
public class ReportSectionController {
	
	@Autowired ReportSectionService reportSectionService;
	@Autowired StudentReportService studentReportService;
	@Autowired EmployerReportService employerReportService;
	
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
		ReportSection reportSection = reportSectionService.createReportSection(reportSectionDto.getTitle(), reportSectionDto.getContent());
		return ControllerUtils.convertToDto(reportSection);
	}
	
	@PutMapping("")
	public ReportSectionDto updateReportSection(@RequestBody ReportSectionDto reportSectionDto) {
		ReportSection reportSection = reportSectionService.getReportSection(reportSectionDto.getId());
		StudentReport studentReport = null;
		EmployerReport employerReport = null;
		
		if (reportSectionDto.getStudentReport() != null) {
			StudentReportDto studentReportDto = reportSectionDto.getStudentReport();
			studentReport = studentReportService.getStudentReport(studentReportDto.getId());
		}
		
		if (reportSectionDto.getEmployerReport() != null) {
			EmployerReportDto employerReportDto = reportSectionDto.getEmployerReport();
			employerReport = employerReportService.getEmployerReport(employerReportDto.getId());
		}
		
		reportSection = reportSectionService.updateReportSection(reportSection, reportSectionDto.getTitle(), reportSectionDto.getContent(), studentReport, employerReport);
		
		return ControllerUtils.convertToDto(reportSection);
	}
	
	@DeleteMapping("/{id}")
	public void deleteReportSection(@PathVariable int id) {
		ReportSection reportSection = reportSectionService.getReportSection(id);
		reportSectionService.deleteReportSection(reportSection);
		
	}

}
