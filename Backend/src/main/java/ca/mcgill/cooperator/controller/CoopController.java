package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.model.Coop;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("coops")
public class CoopController {

    @GetMapping("/{id}")
    public String getCoopById(@PathVariable int id) {
        return "Hello World";
    }

    private CoopDto convertToDto(Coop c) {
        if (c == null) {
            throw new IllegalArgumentException("Coop does not exist!");
        }
        return new CoopDto(
                c.getId(),
                c.getStatus(),
                c.getCourseOffering(),
                c.getCoopDetails(),
                c.getStudent(),
                c.getStudentReports(),
                c.getEmployerReports());
    }
}
