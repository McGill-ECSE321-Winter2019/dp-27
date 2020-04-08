package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import ca.mcgill.cooperator.model.Report;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends BaseService {

    @Autowired AdminRepository adminRepository;
    @Autowired NotificationRepository notificationRepository;

    /**
     * Creates an Admin with a name and email
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return the newly created Admin
     */
    @Transactional
    public Admin createAdmin(String firstName, String lastName, String email) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("Admin first name cannot be empty! ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Admin last name cannot be empty! ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Admin email cannot be empty! ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Admin email must be a valid email!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Admin a = new Admin();
        a.setFirstName(firstName.trim());
        a.setLastName(lastName.trim());
        a.setEmail(email.trim());
        a.setSentNotifications(new HashSet<Notification>());
        a.setReports(new HashSet<Report>());

        return adminRepository.save(a);
    }

    /**
     * Returns the Admin with specified ID
     *
     * @param id
     * @return Admin, if it exists
     */
    @Transactional
    public Admin getAdmin(int id) {
        Admin a = adminRepository.findById(id).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Admin with ID " + id + " does not exist!");
        }

        return a;
    }

    /**
     * Returns the Admin with specified email
     *
     * @param email
     * @return Admin, if it exists
     */
    @Transactional
    public Admin getAdmin(String email) {
        Admin a = adminRepository.findByEmail(email.trim());
        if (a == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Admin with email " + email.trim() + " does not exist!");
        }

        return a;
    }

    /**
     * Returns all Admins that exist
     *
     * @return all Admins
     */
    @Transactional
    public List<Admin> getAllAdmins() {
        return ServiceUtils.toList(adminRepository.findAll());
    }

    /**
     * Updates the specified Admin
     *
     * @param a
     * @param firstName
     * @param lastName
     * @param email
     * @param sentNotifications
     * @return the updated Admin
     */
    @Transactional
    public Admin updateAdmin(
            Admin a,
            String firstName,
            String lastName,
            String email,
            Set<Notification> sentNotifications,
            Set<Report> reports) {
        StringBuilder error = new StringBuilder();
        if (a == null) {
            error.append("Admin to update cannot be null! ");
        }
        if (firstName != null && firstName.trim().length() == 0) {
            error.append("Admin first name cannot be empty! ");
        }
        if (lastName != null && lastName.trim().length() == 0) {
            error.append("Admin last name cannot be empty! ");
        }
        if (email != null && email.trim().length() == 0) {
            error.append("Admin email cannot be empty! ");
        } else if (email != null && !ServiceUtils.isValidEmail(email)) {
            error.append("Admin email must be a valid email!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }
        // set fields if they're not null

        if (firstName != null) {
            a.setFirstName(firstName.trim());
        }
        if (lastName != null) {
            a.setLastName(lastName.trim());
        }
        if (email != null) {
            a.setEmail(email.trim());
        }
        if (sentNotifications != null) {
            a.setSentNotifications(sentNotifications);
        }
        if (reports != null) {
        	a.setReports(reports);
        }

        return adminRepository.save(a);
    }

    /**
     * Deletes the specified Admin
     *
     * @param a
     * @return the deleted Admin
     */
    @Transactional
    public Admin deleteAdmin(Admin a) {
        if (a == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Admin to delete cannot be null!");
        }
        adminRepository.delete(a);

        return a;
    }
}
