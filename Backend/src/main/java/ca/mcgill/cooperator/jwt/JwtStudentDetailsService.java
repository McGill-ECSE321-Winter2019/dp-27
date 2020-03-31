package ca.mcgill.cooperator.jwt;

import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtStudentDetailsService implements UserDetailsService {

    @Autowired private AdminService adminService;

    @Autowired private StudentService studentService;

    /**
     * This method overrides the default UserDetailsService and actually loads by email (which may
     * be confusing given the method name, but it has to be overridden)
     *
     * @param email
     * @return UserDetails containing only the user's email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Student s = studentService.getStudentByEmail(email);

            return new User(s.getEmail(), "", new ArrayList<>());
        } catch (IllegalArgumentException _e) {
            // if we're here then the email is for an Admin, not a Student
            try {
                Admin a = adminService.getAdmin(email);

                return new User(a.getEmail(), "", new ArrayList<>());
            } catch (IllegalArgumentException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        }
    }
}
