package ca.mcgill.cooperator.jwt;

import ca.mcgill.cooperator.model.Student;
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

    @Autowired private StudentService studentService;

    /**
     * Actually loads by email
     *
     * @param email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Student s = studentService.getStudentByEmail(email);
            return new User(s.getEmail(), "", new ArrayList<>());
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
