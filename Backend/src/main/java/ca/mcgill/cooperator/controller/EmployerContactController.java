package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.EmployerContact;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("employer-contacts")
public class EmployerContactController {

    @GetMapping("/{id}")
    public String getEmployerContactById(@PathVariable int id) {
        return "Hello World";
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
