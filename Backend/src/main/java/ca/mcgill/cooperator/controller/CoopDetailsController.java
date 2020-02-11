package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.EmployerContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("coop-details")
public class CoopDetailsController {

    @Autowired CoopDetailsService coopDetailsService;
    @Autowired EmployerContactService employerContactService;
    @Autowired CoopService coopService;

    @GetMapping("/{id}")
    public CoopDetailsDto getCoopDetailsById(@PathVariable int id) {
        return ControllerUtils.convertToDto(coopDetailsService.getCoopDetails(id));
    }

    @GetMapping("")
    public List<CoopDetailsDto> getAllCoopDetails() {
        return ControllerUtils.convertCoopDetailsListToDto(coopDetailsService.getAllCoopDetails());
    }

    @PostMapping("")
    public CoopDetailsDto createCoopDetails(@RequestBody CoopDetailsDto coopDetailsDto) {
        EmployerContactDto employerContactDto = coopDetailsDto.getEmployerContact();
        EmployerContact employerContact =
                employerContactService.getEmployerContact(employerContactDto.getId());

        CoopDto coopDto = coopDetailsDto.getCoop();
        Coop coop = coopService.getCoopById(coopDto.getId());

        CoopDetails coopDetails =
                coopDetailsService.createCoopDetails(
                        coopDetailsDto.getPayPerHour(),
                        coopDetailsDto.getHoursPerWeek(),
                        employerContact,
                        coop);

        return ControllerUtils.convertToDto(coopDetails);
    }
}
