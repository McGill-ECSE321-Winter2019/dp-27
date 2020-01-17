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
@RequestMapping("companies")
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired private EmployerContactService employerContactService;

    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable int id) {
        Company c = companyService.getCompany(id);
        return ControllerUtils.convertToDto(c);
    }

    @GetMapping("")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ControllerUtils.convertCompanyListToDto(companies);
    }

    @PostMapping("")
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {

        List<EmployerContact> employerContacts = new ArrayList<EmployerContact>();
        List<EmployerContactDto> employerContactDtos = companyDto.getEmployees();
        if (employerContactDtos != null) {
            for (EmployerContactDto employerContactDto : employerContactDtos) {
                EmployerContact employerContact =
                        employerContactService.getEmployerContact(employerContactDto.getId());
                employerContacts.add(employerContact);
            }
        }
        Company company =
                companyService.createCompany(
                        companyDto.getName(),
                        companyDto.getCity(),
                        companyDto.getRegion(),
                        companyDto.getCountry(),
                        employerContacts);

        return ControllerUtils.convertToDto(company);
    }

    @PutMapping("")
    public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) {
        Company company = companyService.getCompany(companyDto.getId());

        List<EmployerContactDto> employerContactDtos = companyDto.getEmployees();
        List<EmployerContact> employerContacts = new ArrayList<EmployerContact>();
        if (employerContactDtos != null) {
            for (EmployerContactDto employerContactDto : employerContactDtos) {
                EmployerContact employerContact =
                        employerContactService.getEmployerContact(employerContactDto.getId());
                employerContacts.add(employerContact);
            }
        }

        company =
                companyService.updateCompany(
                        company,
                        companyDto.getName(),
                        companyDto.getCity(),
                        companyDto.getRegion(),
                        companyDto.getCountry(),
                        employerContacts);

        return ControllerUtils.convertToDto(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable int id) {
        Company c = companyService.getCompany(id);
        companyService.deleteCompany(c);
    }
}
