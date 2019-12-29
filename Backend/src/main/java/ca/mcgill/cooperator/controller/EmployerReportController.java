package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.model.EmployerReport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("employer-reports")
public class EmployerReportController {

    @GetMapping("/{id}")
    public String getEmployerReportById(@PathVariable int id) {
        return "Hello World";
    }

    private EmployerReportDto convertToDto(EmployerReport er) {
        if (er == null) {
            throw new IllegalArgumentException("Employer Report does not exist!");
        }
        return new EmployerReportDto(
                er.getId(),
                er.getStatus(),
                er.getCoop(),
                er.getEmployerContact(),
                er.getReportSections());
    }
}
