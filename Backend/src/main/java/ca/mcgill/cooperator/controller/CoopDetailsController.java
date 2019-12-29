package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.model.CoopDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("coop-details")
public class CoopDetailsController {

    @GetMapping("/{id}")
    public String getCoopDetailsById(@PathVariable int id) {
        return "Hello World";
    }

    private CoopDetailsDto convertToDto(CoopDetails cd) {
        if (cd == null) {
            throw new IllegalArgumentException("Coop details do not exist!");
        }
        return new CoopDetailsDto(
                cd.getId(),
                cd.getPayPerHour(),
                cd.getHoursPerWeek(),
                cd.getEmployerContact(),
                cd.getCoop());
    }
}
