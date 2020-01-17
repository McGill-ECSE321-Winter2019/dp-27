package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.EmployerReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("employer-contacts")
public class EmployerContactController {
	
	@Autowired EmployerContactService employerContactService;
	@Autowired CompanyService companyService;
	@Autowired EmployerReportService employerReportService;
	@Autowired CoopDetailsService coopDetailsService;

    @GetMapping("/employer/{id}")
    public EmployerContactDto getEmployerContactById(@PathVariable int id) {
    	EmployerContact ec = employerContactService.getEmployerContact(id);
        return convertToDto(ec);
    }
    
    @PostMapping("/employer/create")
	public EmployerContactDto createEmployerContact(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, 
			@RequestParam String phoneNumber, CompanyDto companyDto) {
    	
    	Company company = companyService.getCompany(companyDto.getId());
		EmployerContact ec = employerContactService.createEmployerContact(firstName, lastName, email, phoneNumber, company);
		return convertToDto(ec);
	}
    
    @PutMapping("/employer/update")
    public EmployerContactDto updateEmployerContact(@RequestParam int id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, 
			@RequestParam String phoneNumber, CompanyDto companyDto, List<EmployerReportDto> employerReportDtos, List<CoopDetailsDto> coopDetailsDtos) {
    	
    	EmployerContact ec = employerContactService.getEmployerContact(id);
    	Company company = companyService.getCompany(companyDto.getId());
    	
    	List<EmployerReport> employerReports = new ArrayList<EmployerReport>();
    	for (EmployerReportDto employerReportDto : employerReportDtos) {
    		EmployerReport er = employerReportService.getEmployerReport(employerReportDto.getId());
    		employerReports.add(er);
    	}
    	
    	List<CoopDetails> coopDetails = new ArrayList<CoopDetails>();
    	for (CoopDetailsDto coopDetailsDto : coopDetailsDtos) {
    		CoopDetails cd = coopDetailsService.getCoopDetails(coopDetailsDto.getId());
    		coopDetails.add(cd);
    	}
    	
    	ec = employerContactService.updateEmployerContact(ec, firstName, lastName, email, phoneNumber, company, employerReports, coopDetails);
    	
    	return convertToDto(ec);
    }
    
    @DeleteMapping("/employer/delete")
    public void deleteEmployerContact(@RequestParam int id) {
    	EmployerContact ec = employerContactService.getEmployerContact(id);
    	employerContactService.deleteEmployerContact(ec);
    }
    

    private EmployerContactDto convertToDto(EmployerContact e) {
        if (e == null) {
            throw new IllegalArgumentException("Employer Contact does not exist!");
        }
        return new EmployerContactDto(
                e.getId(),
                e.getEmail(),
                e.getFirstName(),
                e.getLastName(),
                e.getPhoneNumber(),
                e.getCompany(),
                e.getCoopDetails(),
                e.getEmployerReports());
    }
}
