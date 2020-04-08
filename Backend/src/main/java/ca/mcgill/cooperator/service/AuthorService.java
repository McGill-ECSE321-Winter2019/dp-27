package ca.mcgill.cooperator.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.model.Author;
import ca.mcgill.cooperator.model.Report;

@Service
public class AuthorService extends BaseService {
	@Autowired AuthorRepository authorRepository;
	
	/**
	 * Creates a new author in the database
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	@Transactional
    public Author createAuthor(
            String firstName, String lastName, String email) {
        StringBuilder error = new StringBuilder();
        if (firstName == null || firstName.trim().length() == 0) {
            error.append("Author first name cannot be empty. ");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            error.append("Author last name cannot be empty. ");
        }
        if (email == null || email.trim().length() == 0) {
            error.append("Author email cannot be empty. ");
        } else if (!ServiceUtils.isValidEmail(email)) {
            error.append("Author email must be a valid email. ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Author a = new Author();
        a.setFirstName(firstName.trim());
        a.setLastName(lastName.trim());
        a.setEmail(email.trim());
        a.setReports(new HashSet<Report>());

        return authorRepository.save(a);
    }
	
	@Transactional
    public Author getAuthorById(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + "ID is invalid.");
        }
        Author a = authorRepository.findById(id).orElse(null);
        if (a == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Author with ID " + id + " does not exist.");
        }
        return a;
    }
	
	@Transactional
    public Author updateAuthor(
    		Author a,
            String firstName,
            String lastName,
            String email,
            Set<Report> reports) {

        StringBuilder error = new StringBuilder();
        if (a == null) {
            error.append("Author to update cannot be null. ");
        }
        if (firstName != null && firstName.trim().length() == 0) {
            error.append("Author first name cannot be empty. ");
        }
        if (lastName != null && lastName.trim().length() == 0) {
            error.append("Author last name cannot be empty. ");
        }
        if (email != null && email.trim().length() == 0) {
            error.append("Author email cannot be empty. ");
        } else if (email != null && !ServiceUtils.isValidEmail(email)) {
            error.append("Author email is invalid. ");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (firstName != null) {
            a.setFirstName(firstName.trim());
        }
        if (lastName != null) {
            a.setLastName(lastName.trim());
        }
        if (email != null) {
            a.setEmail(email.trim());
        }
        if (reports!= null) {
        	a.setReports(reports);
        }

        return authorRepository.save(a);
    }
	
	@Transactional
    public Author deleteAuthor(Author a) {
        if (a == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Author to delete cannot be null.");
        }

        authorRepository.delete(a);
        return a;
    }
}
