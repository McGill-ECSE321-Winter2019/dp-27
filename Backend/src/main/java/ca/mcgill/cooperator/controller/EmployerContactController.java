package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.ReportService;
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
    @Autowired ReportService reportService;
    @Autowired CoopDetailsService coopDetailsService;

    /**
     * Creates a new EmployerContact
     *
     * <p>In request body:
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param company
     * @return the created EmployerContact
     */
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

    /**
     * Gets an EmployerContact by ID
     *
     * @param id
     * @return EmployerContactDto object
     */
    @GetMapping("/id/{id}")
    public EmployerContactDto getEmployerContactById(@PathVariable int id) {
        EmployerContact ec = employerContactService.getEmployerContact(id);
        return ControllerUtils.convertToDto(ec);
    }

    /**
     * Gets an EmployerContact by email
     *
     * @param email
     * @return EmployerContactDto object
     */
    @GetMapping("/email/{email}")
    public EmployerContactDto getEmployerContactByEmail(@PathVariable String email) {
        EmployerContact ec = employerContactService.getEmployerContact(email);
        return ControllerUtils.convertToDto(ec);
    }

    /**
     * Gets all EmployerContacts
     *
     * @return List of EmployerContactDtos
     */
    @GetMapping("")
    public List<EmployerContactDto> getAllEmployerContacts() {
        List<EmployerContact> employerContacts = employerContactService.getAllEmployerContacts();
        return ControllerUtils.convertEmployerContactListToDto(employerContacts);
    }

    /**
     * Updates an existing EmployerContact
     *
     * @param id
     *     <p>In request body:
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param company
     * @param employerReports
     * @param coopDetails
     * @return the updated EmployerContact
     */
    @PutMapping("/{id}")
    public EmployerContactDto updateEmployerContact(
            @PathVariable int id, @RequestBody EmployerContactDto employerContactDto) {
        EmployerContact ec = employerContactService.getEmployerContact(id);
        CompanyDto companyDto = employerContactDto.getCompany();
        Company company = null;
        if (companyDto != null) {
            company = companyService.getCompany(companyDto.getId());
        }

        List<ReportDto> reportDtos = employerContactDto.getReports();
        Set<Report> reports = null;
        if (reportDtos != null) {
            reports = ControllerUtils.convertReportDtosToDomainObjects(reportService, reportDtos);
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
                        reports,
                        coopDetails);

        return ControllerUtils.convertToDto(ec);
    }

    /**
     * Deletes an existing EmployerContact
     *
     * @param id
     * @return the deleted EmployerContact
     */
    @DeleteMapping("/{id}")
    public EmployerContactDto deleteEmployerContact(@PathVariable int id) {
        EmployerContact ec = employerContactService.getEmployerContact(id);
        return ControllerUtils.convertToDto(employerContactService.deleteEmployerContact(ec));
    }
}
