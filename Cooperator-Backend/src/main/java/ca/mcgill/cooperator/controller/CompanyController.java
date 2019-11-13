package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("companies")
public class CompanyController {

    @Autowired private CompanyService companyService;

    @GetMapping("/{id}")
    public String getCompanyById(@PathVariable int id) {
        return "Hello World";
    }

    private CompanyDto convertToDto(Company c) {
        if (c == null) {
            throw new IllegalArgumentException("Company does not exist!");
        }
        return new CompanyDto(c.getId(), c.getName(), c.getEmployees());
    }
}
