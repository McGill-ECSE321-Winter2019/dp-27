package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.AuthUserDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Student;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("auth")
public class AuthController {

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
     * Searches for a Student given a user and password
     *
     * <p>user can be in short (e.g. akragl) or long form (e.g. albert.kragl)
     *
     * @param user
     * @param password
     * @return Student if any student matches given credentials, null if none
     */
    @PostMapping("/login/student")
    public StudentDto queryUser(@RequestBody AuthUserDto authUser) {
        try {
            Sam usernameType = getSamType(authUser.getUser());
            if (usernameType != Sam.SHORT_USER && usernameType != Sam.LONG_USER) {
                System.out.println(
                        "Cannot query " + authUser.getUser() + " with type " + usernameType);
                return null;
            }

            String searchName;
            if (usernameType == Sam.LONG_USER) {
                searchName = "userPrincipalName=" + authUser.getUser() + "@mail.mcgill.ca";
            } else {
                searchName = "sAMAccountName=" + authUser.getUser();
            }

            String searchFilter = "(&(objectClass=user)(" + searchName + "))";

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String user;
            if (usernameType == Sam.LONG_USER) user = authUser.getUser() + "@mail.mcgill.ca";
            else user = "CAMPUS\\" + authUser.getUser();

            Hashtable<String, String> env = createAuthMap(user, authUser.getPassword());

            // bind LDAP
            LdapContext ctx = new InitialLdapContext(env, null);

            NamingEnumeration<SearchResult> result =
                    ctx.search(LDAP_BASE, searchFilter, searchControls);
            SearchResult searchResult = result.nextElement();

            Student s = convertToStudent(searchResult, ctx);

            if (ctx != null) {
                ctx.close();
            }

            return ControllerUtils.convertToDto(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Helper methods */

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
            e.printStackTrace();
            return null;
        }
    }

    private String attr(Attributes a, String name) throws NamingException {
        Attribute result = a.get(name);
        return result != null ? result.get().toString() : "";
    }
}
