package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admins")
public class AdminController {

    @Autowired private AdminService adminService;

    @GetMapping("/{id}")
    public String getAdminById(@PathVariable int id) {
        return "Hello World";
    }
}
