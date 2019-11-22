package ca.mcgill.cooperator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.EmployerContact;

@Service
public class CoopDetailsService {
	@Autowired CoopDetailsRepository coopDetailsRepository;
	@Autowired EmployerContactRepository employerContactRepository;
	@Autowired CoopRepository coopRepository;
	
	@Transactional
	public CoopDetails createCoopDetails(int payPerHour, int hoursPerWeek, EmployerContact ec, Coop c) {
		StringBuilder error = new StringBuilder();
        if (payPerHour == 0) {
            error.append("Pay Per Hour cannot be empty! ");
        }
        if (hoursPerWeek == 0) {
            error.append("Hours Per Week cannot be empty! ");
        }
        if (ec == null) {
        	error.append("Employer Contact cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }
        
        CoopDetails cd = new CoopDetails();
        cd.setPayPerHour(payPerHour);
        cd.setHoursPerWeek(hoursPerWeek);
        cd.setEmployerContact(ec);
        cd.setCoop(c);
        
        coopDetailsRepository.save(cd);
        
        List <CoopDetails> employerCoopDetails = ec.getCoopDetails();
        employerCoopDetails.add(cd);
        ec.setCoopDetails(employerCoopDetails);
        
        c.setCoopDetails(cd);
        
        employerContactRepository.save(ec);
        coopRepository.save(c);
        
        return coopDetailsRepository.save(cd);
        
	}
	
	@Transactional
	public List<CoopDetails> getAllCoopDetails() {
		return ServiceUtils.toList(coopDetailsRepository.findAll());
	}
	
	@Transactional
	public CoopDetails deleteCoopDetails(CoopDetails cd) {
		Coop c = cd.getCoop();
		c.setCoopDetails(null);
		coopRepository.save(c);
		coopDetailsRepository.delete(cd);
		return cd;
	}
}
