package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Notification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired AdminRepository adminRepository;

    /**
     * Creates an Admin with a name and email
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return the newly created Admin
     */
    public Admin createAdmin(String firstName, String lastName, String email) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.length() == 0) {
            error.append("Admin first name cannot be empty! ");
        }
        if (lastName == null || lastName.length() == 0) {
            error.append("Admin last name cannot be empty! ");
        }
        if (email == null || email.length() == 0) {
            error.append("Admin email cannot be empty! ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString());
        }

        Admin a = new Admin();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setEmail(email);
        a.setSentNotifications(new ArrayList<Notification>());

        return a;
    }

    /**
     * Creates an Admin with a name, mail and list of sent Notifications
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param sentNotifications
     * @return the newly created Admin
     */
    public Admin createAdmin(
            String firstName, String lastName, String email, List<Notification> sentNotifications) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.length() == 0) {
            error.append("Admin first name cannot be empty! ");
        }
        if (lastName == null || lastName.length() == 0) {
            error.append("Admin last name cannot be empty! ");
        }
        if (email == null || email.length() == 0) {
            error.append("Admin email cannot be empty! ");
        }
        if (sentNotifications == null) {
            error.append("Admin sent notifications cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString());
        }

        Admin a = new Admin();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setEmail(email);
        a.setSentNotifications(sentNotifications);

        return a;
    }

    /**
     * Returns the Admin with specified ID
     *
     * @param id
     * @return Admin, if it exists
     */
    public Admin getAdmin(int id) {
        Admin a = adminRepository.findById(id).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException("Admin with ID " + id + " does not exist!");
        }

        return a;
    }

    /**
     * Returns the Admin with specified email
     *
     * @param email
     * @return Admin, if it exists
     */
    public Admin getAdmin(String email) {
        Admin a = adminRepository.findByEmail(email);
        if (a == null) {
            throw new IllegalArgumentException("Admin with email " + email + " does not exist!");
        }

        return a;
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
    public Admin updateAdmin(
            Admin a,
            String firstName,
            String lastName,
            String email,
            List<Notification> sentNotifications) {
        StringBuilder error = new StringBuilder();
        if (a == null) {
            error.append("Admin to update cannot be null! ");
        }
        if (lastName == null || lastName.length() == 0) {
            error.append("Admin last name cannot be empty! ");
        }
        if (email == null || email.length() == 0) {
            error.append("Admin email cannot be empty! ");
        }
        if (sentNotifications == null) {
            error.append("Admin sent notifications cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString());
        }

        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setEmail(email);
        a.setSentNotifications(sentNotifications);
        adminRepository.save(a);

        return a;
    }

    /**
     * Deletes the specified Admin
     *
     * @param a
     * @return the deleted Admin
     */
    public Admin deleteAdmin(Admin a) {
        if (a == null) {
            throw new IllegalArgumentException("Admin to delete cannot be null!");
        }
        adminRepository.delete(a);

        return a;
    }
}
