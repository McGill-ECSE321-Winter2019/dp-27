package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.NotificationService;
import ca.mcgill.cooperator.service.ReportService;

import java.util.List;
import java.util.Set;

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
    @Autowired private ReportService reportService;

    /**
     * Creates a new Admin
     *
     * <p>In request body:
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return the created Admin
     */
    @PostMapping("")
    public AdminDto createAdmin(@RequestBody AdminDto a) {
        Admin createdAdmin =
                adminService.createAdmin(a.getFirstName(), a.getLastName(), a.getEmail());

        return ControllerUtils.convertToDto(createdAdmin);
    }

    /**
     * Gets all Admins
     *
     * @return List of AdminDto objects
     */
    @GetMapping("")
    public List<AdminDto> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();

        return ControllerUtils.convertAdminListToDto(admins);
    }

    /**
     * Gets an Admin by ID
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
     * Updates an existing Admin
     *
     * <p>In request body:
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param sentNotifications
     * @return the updated Admin
     */
    @PutMapping("/{id}")
    public AdminDto updateAdmin(@PathVariable int id, @RequestBody AdminDto a) {
        Admin admin = adminService.getAdmin(id);

        Set<Notification> notifs = null;

        if (a.getSentNotifications() != null) {
            notifs =
                    ControllerUtils.convertNotificationListToDomainObjectSet(
                            notifService, a.getSentNotifications());
        }
        
        Set<Report> reports = null;
        
        if (a.getReports() != null) {
        	reports = ControllerUtils.convertReportDtosToDomainObjects(reportService, a.getReports());
        }

        Admin updatedAdmin =
                adminService.updateAdmin(
                        admin, a.getFirstName(), a.getLastName(), a.getEmail(), notifs, reports);

        return ControllerUtils.convertToDto(updatedAdmin);
    }

    /**
     * Deletes an existing Admin
     *
     * @param id
     * @return the deleted Admin
     */
    @DeleteMapping("/{id}")
    public AdminDto deleteAdmin(@PathVariable int id) {
        Admin admin = adminService.getAdmin(id);
        Admin deletedAdmin = adminService.deleteAdmin(admin);

        return ControllerUtils.convertToDto(deletedAdmin);
    }
}
