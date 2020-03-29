package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoopDetailsService extends BaseService {

    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CoopRepository coopRepository;

    /**
     * Creates a new CoopDetails in database
     *
     * @param payPerHour
     * @param hoursPerWeek
     * @param startDate
     * @param endDate
     * @param ec
     * @param c
     * @return the created CoopDetails
     */
    @Transactional
    public CoopDetails createCoopDetails(
            int payPerHour,
            int hoursPerWeek,
            Date startDate,
            Date endDate,
            EmployerContact ec,
            Coop c) {
        StringBuilder error = new StringBuilder();
        if (payPerHour < 0) {
            error.append("Pay Per Hour is invalid! ");
        }
        if (hoursPerWeek <= 0) {
            error.append("Hours Per Week is invalid! ");
        }
        if (startDate == null) {
            error.append("Start date cannot be null! ");
        }
        if (endDate == null) {
            error.append("End date cannot be null! ");
        }
        if (ec == null) {
            error.append("Employer Contact cannot be null! ");
        }
        if (c == null) {
            error.append("Co-op cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        CoopDetails cd = new CoopDetails();
        cd.setPayPerHour(payPerHour);
        cd.setHoursPerWeek(hoursPerWeek);
        cd.setStartDate(startDate);
        cd.setEndDate(endDate);
        cd.setEmployerContact(ec);
        cd.setCoop(c);

        return coopDetailsRepository.save(cd);
    }

    /**
     * Retrieves the CoopDetails with specified id
     *
     * @param id
     * @return CoopDetails with specified ID
     */
    @Transactional
    public CoopDetails getCoopDetails(int id) {
        CoopDetails cd = coopDetailsRepository.findById(id).orElse(null);
        if (cd == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Co-op Details with ID " + id + " does not exist!");
        }

        return cd;
    }

    /**
     * Retrieves all CoopDetails entities from database
     *
     * @return all CoopDetails
     */
    @Transactional
    public List<CoopDetails> getAllCoopDetails() {
        return ServiceUtils.toList(coopDetailsRepository.findAll());
    }

    /**
     * Updates an existing CoopDetails
     *
     * @param cd
     * @param payPerHour
     * @param hoursPerWeek
     * @param startDate
     * @param endDate
     * @param ec
     * @param c
     * @return the updated CoopDetails
     */
    @Transactional
    public CoopDetails updateCoopDetails(
            CoopDetails cd,
            Integer payPerHour,
            Integer hoursPerWeek,
            Date startDate,
            Date endDate,
            EmployerContact ec,
            Coop c) {
        StringBuilder error = new StringBuilder();
        if (cd == null) {
            error.append("Co-op Details to update cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (payPerHour != null && payPerHour >= 0) {
            cd.setPayPerHour(payPerHour);
        }
        if (hoursPerWeek != null && hoursPerWeek > 0) {
            cd.setHoursPerWeek(hoursPerWeek);
        }
        if (startDate != null) {
            cd.setStartDate(startDate);
        }
        if (endDate != null) {
            cd.setEndDate(endDate);
        }
        if (ec != null) {
            cd.setEmployerContact(ec);
        }
        if (c != null) {
            cd.setCoop(c);
        }

        return coopDetailsRepository.save(cd);
    }

    /**
     * Deletes an existing CoopDetails
     *
     * @param cd
     * @return the deleted CoopDetails
     */
    @Transactional
    public CoopDetails deleteCoopDetails(CoopDetails cd) {
        if (cd == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Co-op Details to delete cannot be null!");
        }
        Coop c = cd.getCoop();
        c.setCoopDetails(null);
        coopRepository.save(c);

        EmployerContact ec = cd.getEmployerContact();
        Set<CoopDetails> details = new HashSet<>(ec.getCoopDetails());
        details.remove(cd);
        ec.setCoopDetails(details);
        employerContactRepository.save(ec);

        coopDetailsRepository.delete(cd);
        return cd;
    }
}
