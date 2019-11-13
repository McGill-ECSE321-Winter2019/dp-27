package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.model.Admin;
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

    private AdminDto convertToDto(Admin a) {
        if (a == null) {
            throw new IllegalArgumentException("Admin does not exist!");
        }
        return new AdminDto(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getEmail(),
                a.getSentNotifications());
    }
}
