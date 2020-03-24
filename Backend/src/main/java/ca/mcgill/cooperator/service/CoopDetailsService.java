package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;

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
     * creates a new coop details in database
     *
     * @param payPerHour
     * @param hoursPerWeek
     * @param ec
     * @param c
     * @return coop details
     */
    @Transactional
    public CoopDetails createCoopDetails(
            int payPerHour, int hoursPerWeek, EmployerContact ec, Coop c) {
        StringBuilder error = new StringBuilder();
        if (payPerHour < 0) {
            error.append("Pay Per Hour is invalid! ");
        }
        if (hoursPerWeek <= 0) {
            error.append("Hours Per Week is invalid! ");
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
        cd.setEmployerContact(ec);
        cd.setCoop(c);

        return coopDetailsRepository.save(cd);
    }

    /**
     * retrieves coop details with specific id
     *
     * @param id
     * @return coop details
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
     * retrieves all coop details entities from database
     *
     * @return list of coop details
     */
    @Transactional
    public List<CoopDetails> getAllCoopDetails() {
        return ServiceUtils.toList(coopDetailsRepository.findAll());
    }

    /**
     * delete specific coop details
     *
     * @param cd
     * @return deleted coop details
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

    /**
     * update exisitng coop details
     *
     * @param cd
     * @param payPerHour
     * @param hoursPerWeek
     * @param ec
     * @param c
     * @return updated coop details
     */
    @Transactional
    public CoopDetails updateCoopDetails(
            CoopDetails cd, int payPerHour, int hoursPerWeek, EmployerContact ec, Coop c) {
        StringBuilder error = new StringBuilder();
        if (cd == null) {
            error.append("Co-op Details to update cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (payPerHour >= 0) {
            cd.setPayPerHour(payPerHour);
        }
        if (hoursPerWeek > 0) {
            cd.setHoursPerWeek(hoursPerWeek);
        }
        if (ec != null) {
            cd.setEmployerContact(ec);
        }
        if (c != null) {
            cd.setCoop(c);
        }

        return coopDetailsRepository.save(cd);
    }
}
