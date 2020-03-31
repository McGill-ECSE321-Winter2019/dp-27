package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.jwt.JwtRequest;
import ca.mcgill.cooperator.jwt.JwtStudentDetailsService;
import ca.mcgill.cooperator.jwt.JwtTokenUtil;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired private StudentService studentService;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired private JwtStudentDetailsService userDetailsService;

    @PostMapping("/authenticate/student")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest authRequest) throws Exception {
        Student student = authenticate(authRequest.getUser(), authRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(student.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }

    private Student authenticate(String username, String password) throws Exception {
        try {
            Authentication auth =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password));
            String email = auth.getPrincipal().toString();

            return studentService.getStudentByEmail(email);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
