package ca.mcgill.cooperator.controller;

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
}
