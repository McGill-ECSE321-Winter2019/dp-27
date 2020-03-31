package ca.mcgill.cooperator.jwt;

import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.StudentService;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * This class overrides the default Spring AuthenticationProvider and authenticates users using the
 * McGill LDAP system.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired StudentService studentService;

    enum Sam {
        SHORT_USER,
        LONG_USER,
        STUDENT_ID,
        NONE
    }

    private static final String LDAP_BASE = "dc=campus,dc=mcgill,dc=ca";

    private String shortUserRegex = "[a-zA-Z]+[0-9]*";
    private String studentIdRegex = "[0-9]+";
    private String longUserRegex = "[a-zA-Z]+(\\.[a-zA-Z]+)+";

    /**
     * Overrides the default authenticate method that comes with Spring Security.
     *
     * @param authentication
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String user = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Student student = queryUser(user, password);
        if (student == null) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        return new UsernamePasswordAuthenticationToken(student.getEmail(), null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /* Helper methods */

    private Student queryUser(String user, String password) {
        try {
            Sam usernameType = getSamType(user);
            if (usernameType != Sam.SHORT_USER && usernameType != Sam.LONG_USER) {
                System.out.println("Cannot query " + user + " with type " + usernameType);
                return null;
            }

            String searchName;
            if (usernameType == Sam.LONG_USER) {
                searchName = "userPrincipalName=" + user + "@mail.mcgill.ca";
            } else {
                searchName = "sAMAccountName=" + user;
            }

            String searchFilter = "(&(objectClass=user)(" + searchName + "))";

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String userForAuthMap;
            if (usernameType == Sam.LONG_USER) userForAuthMap = user + "@mail.mcgill.ca";
            else userForAuthMap = "CAMPUS\\" + user;

            Hashtable<String, String> env = createAuthMap(userForAuthMap, password);

            // bind LDAP
            LdapContext ctx = new InitialLdapContext(env, null);

            NamingEnumeration<SearchResult> result =
                    ctx.search(LDAP_BASE, searchFilter, searchControls);
            SearchResult searchResult = result.nextElement();

            Student s = convertToStudent(searchResult, ctx);

            if (ctx != null) {
                ctx.close();
            }

            return s;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private Sam getSamType(String sam) {
        if (Pattern.matches(shortUserRegex, sam)) return Sam.SHORT_USER;
        if (Pattern.matches(longUserRegex, sam)) return Sam.LONG_USER;
        if (Pattern.matches(studentIdRegex, sam)) return Sam.STUDENT_ID;
        else return Sam.NONE;
    }

    private Hashtable<String, String> createAuthMap(String user, String password) {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://campus.mcgill.ca:389");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);

        return env;
    }

    private Student convertToStudent(SearchResult searchResult, LdapContext ctx) {
        try {
            Attributes attributes = searchResult.getAttributes();

            Student s = new Student();
            s.setEmail(attr(attributes, "mail"));
            s.setFirstName(attr(attributes, "givenName"));
            s.setLastName(attr(attributes, "sn"));

            return s;
        } catch (NamingException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private String attr(Attributes a, String name) throws NamingException {
        Attribute result = a.get(name);
        return result != null ? result.get().toString() : "";
    }
}
