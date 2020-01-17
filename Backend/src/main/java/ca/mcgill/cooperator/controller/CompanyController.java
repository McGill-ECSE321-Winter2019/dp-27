package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.EmployerContactService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("companies")
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired private EmployerContactService employerContactService;

    @GetMapping("/company/{id}")
    public CompanyDto getCompanyById(@PathVariable int id) {
    	Company c = companyService.getCompany(id);
        return convertToDto(c);
    }
    
    @GetMapping("/hello")
    public String helloWorld() {
    	
        return "hello bish";
    }
    
    @PostMapping("/company/create")
    public CompanyDto createCompany(@RequestParam String name, @RequestParam(required = false)List<EmployerContactDto> employerContactDtos) {
    	List<EmployerContact> employerContacts = new ArrayList<EmployerContact>();
    	if (employerContactDtos != null) {
	    	for (EmployerContactDto employerContactDto : employerContactDtos) {
	    		EmployerContact ec = employerContactService.getEmployerContact(employerContactDto.getId());
	    		employerContacts.add(ec);
	    	}
    	}
    	Company company = companyService.createCompany(name, employerContacts);
    	
    	return convertToDto(company);
    }

    private CompanyDto convertToDto(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company does not exist!");
        }
        return new CompanyDto(c.getId(), c.getName(), c.getEmployees());
    }
}
