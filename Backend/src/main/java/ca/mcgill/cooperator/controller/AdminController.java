package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.service.AdminService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admins")
public class AdminController {

    @Autowired private AdminService adminService;
    
    /**
     * Get all Admins
     * 
     * @return List of AdminDto objects
     */
    @GetMapping("")
    public List<AdminDto> getAllAdmins() {
    	List<Admin> admins = adminService.getAllAdmins();
    	
        return convertToDto(admins);
    }

    /**
     * Get an Admin by ID
     * 
     * @param id
     * @return AdminDto object
     */
    @GetMapping("/{id}")
    public AdminDto getAdminById(@PathVariable int id) {
    	Admin a = adminService.getAdmin(id);
    	
        return convertToDto(a);
    }
    
    /**
     * Create a new Admin
     * 
     * In request body:
     * @param firstName 
     * @param lastName
     * @param email
     * @return created Admin
     */
    @PostMapping("")
    public AdminDto createAdmin(@RequestBody AdminDto a) {
    	Admin createdAdmin = adminService.createAdmin(a.getFirstName(), a.getLastName(), a.getEmail());
    	
    	return convertToDto(createdAdmin);
    }
    
    /**
     * Update an existing Admin
     * 
     * In request body:
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param sentNotifications
     * @return updated Admin
     */
    @PutMapping("")
    public AdminDto updateAdmin(@RequestBody AdminDto a) {
    	Admin admin = adminService.getAdmin(a.getId());
    	Admin updatedAdmin = adminService.updateAdmin(admin, a.getFirstName(), a.getLastName(), a.getEmail(), a.getSentNotifications());
    	
    	return convertToDto(updatedAdmin);
    }
}
