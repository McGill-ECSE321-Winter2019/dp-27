package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.jwt.JwtRequest;
import ca.mcgill.cooperator.jwt.JwtStudentDetailsService;
import ca.mcgill.cooperator.jwt.JwtTokenUtil;
import ca.mcgill.cooperator.model.Admin;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.AdminService;
import ca.mcgill.cooperator.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	@Autowired private AdminService adminService;

    @Autowired private StudentService studentService;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired private JwtStudentDetailsService userDetailsService;

    /**
     * Logs a user in by generating a token for them after the authenticate successfully with the
     * McGill LDAP system.
     * 
     * @param authRequest
     * @return the generated token for the user
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest authRequest) throws Exception {
        Student student = authenticate(authRequest.getUser(), authRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(student.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(token);
    }
    
    /**
     * Accepts a token in the body of the request and checks if it is associated with any user
     * 
     * @param token in body
     * @return user if token matches
     * @throws Exception
     */
    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody JwtRequest tokenRequest) throws Exception {
        String email = jwtTokenUtil.getSubjectFromToken(tokenRequest.getToken());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        
        try {
        	Student s = studentService.getStudentByEmail(userDetails.getUsername());
        	
        	return ResponseEntity.ok(ControllerUtils.convertToDto(s));
        } catch (IllegalArgumentException _e) {
        	try {
        		Admin a = adminService.getAdmin(userDetails.getUsername());
            	
            	return ResponseEntity.ok(ControllerUtils.convertToDto(a));
        	} catch (IllegalArgumentException e) {
        		return new ResponseEntity<String>("Token does not match any logged in user", HttpStatus.BAD_REQUEST);
        	}	
        }
    }

    private Student authenticate(String user, String password) throws Exception {
        try {
            Authentication auth =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user, password));
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
