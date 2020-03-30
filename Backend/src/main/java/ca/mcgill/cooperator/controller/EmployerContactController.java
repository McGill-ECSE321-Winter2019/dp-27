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
@RequestMapping("employer-contacts")
public class EmployerContactController extends BaseController {

    @Autowired EmployerContactService employerContactService;
    @Autowired CompanyService companyService;
    @Autowired EmployerReportService employerReportService;
    @Autowired CoopDetailsService coopDetailsService;

    @GetMapping("/{id}")
    public EmployerContactDto getEmployerContactById(@PathVariable int id) {
        EmployerContact ec = employerContactService.getEmployerContact(id);
        return ControllerUtils.convertToDto(ec);
    }

    @GetMapping("")
    public List<EmployerContactDto> getAllEmployerContacts() {
        List<EmployerContact> employerContacts = employerContactService.getAllEmployerContacts();
        return ControllerUtils.convertEmployerContactListToDto(employerContacts);
    }

    @PostMapping("")
    public EmployerContactDto createEmployerContact(
            @RequestBody EmployerContactDto employerContactDto) {
        CompanyDto companyDto = employerContactDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.getCompany(companyDto.getId());
        }
        EmployerContact ec =
                employerContactService.createEmployerContact(
                        employerContactDto.getFirstName(),
                        employerContactDto.getLastName(),
                        employerContactDto.getEmail(),
                        employerContactDto.getPhoneNumber(),
                        company);
        return ControllerUtils.convertToDto(ec);
    }

    @PutMapping("/{id}")
    public EmployerContactDto updateEmployerContact(
            @PathVariable int id, @RequestBody EmployerContactDto employerContactDto) {

        EmployerContact ec = employerContactService.getEmployerContact(id);
        CompanyDto companyDto = employerContactDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.getCompany(companyDto.getId());
        }

        List<EmployerReportDto> employerReportDtos = employerContactDto.getEmployerReports();
        Set<EmployerReport> employerReports = null;
        if (employerReportDtos != null) {
            employerReports =
                    ControllerUtils.convertEmployerReportDtosToDomainObjects(
                            employerReportService, employerReportDtos);
        }

        List<CoopDetailsDto> coopDetailsDtos = employerContactDto.getCoopDetails();
        Set<CoopDetails> coopDetails = null;
        if (coopDetailsDtos != null) {
            coopDetails =
                    ControllerUtils.convertCoopDetailsDtosToDomainObjects(
                            coopDetailsService, coopDetailsDtos);
        }

        ec =
                employerContactService.updateEmployerContact(
                        ec,
                        employerContactDto.getFirstName(),
                        employerContactDto.getLastName(),
                        employerContactDto.getEmail(),
                        employerContactDto.getPhoneNumber(),
                        company,
                        employerReports,
                        coopDetails);

        return ControllerUtils.convertToDto(ec);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployerContact(@PathVariable int id) {
        EmployerContact ec = employerContactService.getEmployerContact(id);
        employerContactService.deleteEmployerContact(ec);
    }
}
