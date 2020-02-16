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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("companies")
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired private EmployerContactService employerContactService;

    /**
     * Gets a Company by ID
     *
     * @param id
     * @return Company with specified ID
     */
    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable int id) {
        Company c = companyService.getCompany(id);
        return ControllerUtils.convertToDto(c);
    }

    /**
     * Gets all Companies or all Companies with specified name
     *
     * @param name
     * @return Companies that match filter
     */
    @GetMapping("")
    public List<CompanyDto> getAllCompanies(@RequestParam(required = false) String name) {
        if (name == null) {
            // return all companies
            List<Company> companies = companyService.getAllCompanies();
            return ControllerUtils.convertCompanyListToDto(companies);
        } else {
            // filter by name
            List<Company> companies = companyService.getCompanies(name);
            return ControllerUtils.convertCompanyListToDto(companies);
        }
    }

    /**
     * Creates a new Company
     *
     * @param name
     * @param city
     * @param region
     * @param county
     * @return created Company
     */
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

    /**
     * Updates an existing Company
     *
     * @param name
     * @param city
     * @param region
     * @param county
     * @param employees
     * @return updated Company
     */
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

    /**
     * Deletes a Company by ID
     *
     * @param id
     * @return deleted Company
     */
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable int id) {
        Company c = companyService.getCompany(id);
        companyService.deleteCompany(c);
    }
}
