package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CoopService;
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
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("coop-details")
public class CoopDetailsController extends BaseController {

    @Autowired CoopDetailsService coopDetailsService;
    @Autowired CompanyService companyService;
    @Autowired EmployerContactService employerContactService;
    @Autowired CoopService coopService;

    /**
     * Creates a new CoopDetails
     *
     * <p>In request body:
     *
     * @param payPerHour
     * @param hoursPerWeek
     * @param startDate
     * @param endDate
     * @param employerContact
     * @param coop
     * @return the created CoopDetails
     */
    @PostMapping("")
    public CoopDetailsDto createCoopDetails(@RequestBody CoopDetailsDto coopDetailsDto) {
        EmployerContactDto employerContactDto = coopDetailsDto.getEmployerContact();
        EmployerContact employerContact = null;
        if (employerContactDto != null) {
            if (employerContactDto.getId() != null) {
            	try {
            		employerContact =
                            employerContactService.getEmployerContact(employerContactDto.getId());
            	} catch (IllegalArgumentException e) {
            		// create the EmployerContact (and Company, if necessary)
                    employerContact = handleCreateEmployerContactAndCompany(employerContactDto);
            	}
            } else if (employerContactDto.getEmail() != null) {
            	try {
                    employerContact =
                            employerContactService.getEmployerContact(
                                    employerContactDto.getEmail());
                } catch (IllegalArgumentException e) {
                    // create the EmployerContact (and Company, if necessary)
                    employerContact = handleCreateEmployerContactAndCompany(employerContactDto);
                }
            }
        }

        CoopDto coopDto = coopDetailsDto.getCoop();
        Coop coop = null;
        if (coopDto != null) {
            coop = coopService.getCoopById(coopDto.getId());
        }

        CoopDetails coopDetails =
                coopDetailsService.createCoopDetails(
                        coopDetailsDto.getPayPerHour(),
                        coopDetailsDto.getHoursPerWeek(),
                        coopDetailsDto.getStartDate(),
                        coopDetailsDto.getEndDate(),
                        employerContact,
                        coop);

        return ControllerUtils.convertToDto(coopDetails);
    }

    /**
     * Gets a CoopDetails by ID
     *
     * @param id
     * @return CoopDetailsDto object
     */
    @GetMapping("/{id}")
    public CoopDetailsDto getCoopDetailsById(@PathVariable int id) {
        return ControllerUtils.convertToDto(coopDetailsService.getCoopDetails(id));
    }

    /**
     * Gets all CoopDetails
     *
     * @return list of CoopDetailsDtos
     */
    @GetMapping("")
    public List<CoopDetailsDto> getAllCoopDetails() {
        return ControllerUtils.convertCoopDetailsListToDto(coopDetailsService.getAllCoopDetails());
    }

    /**
     * Updates an existing CoopDetails
     *
     * @param id
     *     <p>In request body:
     * @param payPerHour
     * @param hoursPerWeek
     * @param startDate
     * @param endDate
     * @param employerContact
     * @param coop
     * @return the updated CoopDetails
     */
    @PutMapping("/{id}")
    public CoopDetailsDto updateCoopDetails(
            @PathVariable int id, @RequestBody CoopDetailsDto coopDetailsDto) {
        EmployerContactDto employerContactDto = coopDetailsDto.getEmployerContact();
        EmployerContact employerContact = null;
        if (employerContactDto != null) {
            if (employerContactDto.getId() != null) {
            	try {
            		employerContact =
                            employerContactService.getEmployerContact(employerContactDto.getId());
            	} catch (IllegalArgumentException e) {
            		// create the EmployerContact (and Company, if necessary)
                    employerContact = handleCreateEmployerContactAndCompany(employerContactDto);
            	}
            } else if (employerContactDto.getEmail() != null) {
                try {
                    employerContact =
                            employerContactService.getEmployerContact(
                                    employerContactDto.getEmail());
                } catch (IllegalArgumentException e) {
                    // create the EmployerContact (and Company, if necessary)
                    employerContact = handleCreateEmployerContactAndCompany(employerContactDto);
                }
            }
        }

        CoopDto coopDto = coopDetailsDto.getCoop();
        Coop coop = null;
        if (coopDto != null) {
            coop = coopService.getCoopById(coopDto.getId());
        }

        CoopDetails coopDetails = coopDetailsService.getCoopDetails(id);

        coopDetails =
                coopDetailsService.updateCoopDetails(
                        coopDetails,
                        coopDetailsDto.getPayPerHour(),
                        coopDetailsDto.getHoursPerWeek(),
                        coopDetailsDto.getStartDate(),
                        coopDetailsDto.getEndDate(),
                        employerContact,
                        coop);

        return ControllerUtils.convertToDto(coopDetails);
    }

    /**
     * Deletes an existing CoopDetails
     *
     * @param id
     * @return the deleted CoopDetails
     */
    @DeleteMapping("/{id}")
    public CoopDetailsDto deleteCoopDetails(@PathVariable int id) {
        CoopDetails cd = coopDetailsService.getCoopDetails(id);
        return ControllerUtils.convertToDto(coopDetailsService.deleteCoopDetails(cd));
    }
    
    private EmployerContact handleCreateEmployerContactAndCompany(EmployerContactDto ecDto) {
        CompanyDto cDto = ecDto.getCompany();

        Company company;
        try {
            company =
                    companyService.getCompany(
                            cDto.getName(),
                            cDto.getCity(),
                            cDto.getRegion(),
                            cDto.getCountry());
        } catch (IllegalArgumentException _e) {
            company =
                    companyService.createCompany(
                            cDto.getName(),
                            cDto.getCity(),
                            cDto.getRegion(),
                            cDto.getCountry(),
                            null);
        }

        return employerContactService.createEmployerContact(
        		ecDto.getFirstName(),
        		ecDto.getLastName(),
        		ecDto.getEmail(),
        		ecDto.getPhoneNumber(),
                company);
    }
}
