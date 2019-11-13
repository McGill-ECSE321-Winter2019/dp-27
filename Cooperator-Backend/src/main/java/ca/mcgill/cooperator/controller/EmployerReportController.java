package ca.mcgill.cooperator.controller;

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
}
