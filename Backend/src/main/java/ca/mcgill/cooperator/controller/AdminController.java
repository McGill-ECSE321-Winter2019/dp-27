package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.NotificationService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class AdminController extends BaseController {

    @Autowired private AdminService adminService;
    @Autowired private NotificationService notifService;

    /**
     * Get all Admins
     *
     * @return List of AdminDto objects
     */
    @GetMapping("")
    public List<AdminDto> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();

        return ControllerUtils.convertAdminListToDto(admins);
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

        return ControllerUtils.convertToDto(a);
    }

    /**
     * Create a new Admin
     *
     * <p>In request body:
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return created Admin
     */
    @PostMapping("")
    public AdminDto createAdmin(@RequestBody AdminDto a) {
        Admin createdAdmin =
                adminService.createAdmin(a.getFirstName(), a.getLastName(), a.getEmail());

        return ControllerUtils.convertToDto(createdAdmin);
    }

    /**
     * Update an existing Admin
     *
     * <p>In request body:
     *
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
        Admin updatedAdmin =
                adminService.updateAdmin(
                        admin,
                        a.getFirstName(),
                        a.getLastName(),
                        a.getEmail(),
                        ControllerUtils.convertNotificationListToDomainObject(
                                notifService, a.getSentNotifications()));

        return ControllerUtils.convertToDto(updatedAdmin);
    }

    /**
     * Delete an existing Admin
     *
     * @param id
     * @return deleted Admin
     */
    @DeleteMapping("/{id}")
    public AdminDto deleteAdmin(@PathVariable int id) {
        Admin admin = adminService.getAdmin(id);
        Admin deletedAdmin = adminService.deleteAdmin(admin);

        return ControllerUtils.convertToDto(deletedAdmin);
    }
}
