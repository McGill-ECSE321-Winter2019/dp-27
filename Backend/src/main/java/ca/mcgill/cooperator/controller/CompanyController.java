package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.EmployerContactService;
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
public class CompanyController extends BaseController {

    @Autowired private CompanyService companyService;
    @Autowired private EmployerContactService employerContactService;
    
    /**
     * Creates a new Company
     *
     * @param name
     * @param city
     * @param region
     * @param county
     * @return the created Company
     */
    @PostMapping("")
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        List<EmployerContact> employerContacts = null;
        List<EmployerContactDto> employerContactDtos = companyDto.getEmployees();
        if (employerContactDtos != null) {
            employerContacts =
                    ControllerUtils.covertEmployerContactDtosToDomainObjects(
                            employerContactService, employerContactDtos);
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
     * Gets a Company by ID
     *
     * @param id
     * @return CompanyDto object
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
     * @return List of CompanyDtos that match filter
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
     * Updates an existing Company
     *
     * @param name
     * @param city
     * @param region
     * @param county
     * @param employees
     * @return the updated Company
     */
    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable int id, @RequestBody CompanyDto companyDto) {
        Company company = companyService.getCompany(id);

        List<EmployerContactDto> employerContactDtos = companyDto.getEmployees();
        List<EmployerContact> employerContacts = null;
        if (employerContactDtos != null) {
            employerContacts =
                    ControllerUtils.covertEmployerContactDtosToDomainObjects(
                            employerContactService, employerContactDtos);
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
     * @return the deleted Company
     */
    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable int id) {
        Company c = companyService.getCompany(id);
        companyService.deleteCompany(c);
    }
}
