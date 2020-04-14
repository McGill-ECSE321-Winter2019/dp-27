package ca.mcgill.cooperator.schedule;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.EmployerContactService;

import java.net.URLEncoder;
import java.security.Key;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class contains jobs that should run in the background on an automated basis. Job frequency
 * should be specified with a cron expression.
 */
@Component
@EnableAsync
public class NightlyTasks {

    @Autowired CoopService coopService;
    @Autowired EmployerContactService employerContactService;

    static final Logger logger = Logger.getLogger(NightlyTasks.class);

    // runs once every weekday at 4am
    static final String CRON_WEEKDAY = "0 0 4 * * MON-FRI";

    // runs every 15 minutes, use this for testing
    static final String CRON_TEST = "0 */15 * ? * *";

    /** Checks all existing Coops and updates their status if necessary. */
    @Async
    @Scheduled(cron = CRON_TEST)
    public void updateCoopStatuses() {
        List<Coop> coops = coopService.getAllCoops();

        Date currentDate = new Date(System.currentTimeMillis());
        Date coopDate;

        if (logger.isInfoEnabled()) {
            logger.info("Running job to update Coop statuses");
        }

        for (Coop coop : coops) {
            if (coop.getStatus() == CoopStatus.FUTURE) {
                if (coop.getCoopDetails() == null) continue;

                coopDate = coop.getCoopDetails().getStartDate();
                // check if the co-op starts today or has already started
                if (currentDate.compareTo(coopDate) >= 0) {
                    // update status to IN_PROGRESS
                    coopService.updateCoop(coop, CoopStatus.IN_PROGRESS, null, null, null, null);
                }
            } else if (coop.getStatus() == CoopStatus.IN_PROGRESS) {
                coopDate = coop.getCoopDetails().getEndDate();
                // check if the co-op has ended
                if (currentDate.compareTo(coopDate) > 0) {
                    // update status to FINISHED
                    coopService.updateCoop(coop, CoopStatus.FINISHED, null, null, null, null);
                }
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("Completed job to update Coop statuses");
        }
    }
    
    @Async
    @Scheduled(cron = CRON_TEST)
    public void sendEmployerContactLinks() throws Exception {
    	//find employer contacts that have reports to complete
    	//for now just do all employer contacts in the system that have active coops
    	if (logger.isInfoEnabled()) {
            logger.info("Running job to generate EmployerContact links");
        }
    	
    	List<EmployerContact> employerContacts = employerContactService.getAllEmployerContacts();
    	
    	List<String> links = new ArrayList<String>();
    	
    	for (EmployerContact employerContact: employerContacts) {
    		Set<CoopDetails> coopDetails = employerContact.getCoopDetails();
    		for (CoopDetails cd : coopDetails) {
    			//if coop in progress, generate link
    			if (cd.getCoop().getStatus() == CoopStatus.IN_PROGRESS) {
    				String text = employerContact.getEmail();
    	            String key = "PeShVmYq3t6w9z$C"; // 128 bit key
    	            // Create key and cipher
    	            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
    	            System.err.println(aesKey.toString());
    	            Cipher cipher = null;
    	            byte[] encrypted = null;
    	            
					try {
						cipher = Cipher.getInstance("AES");
						 // encrypt the text
						cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	    	            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					
    				//localhost for now, switch to actual website later
    				String link = "localhost:8080/#/employer-contact/" + URLEncoder.encode(Base64.getEncoder().encodeToString(encrypted), "UTF-8");
    				links.add(link);
    				break;
    			}
    		}
    	}
    	
    	if (logger.isInfoEnabled()) {
            logger.info(links.toString());
        }
    	
    }

}
