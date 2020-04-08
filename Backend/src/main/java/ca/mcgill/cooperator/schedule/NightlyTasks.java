package ca.mcgill.cooperator.schedule;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.service.CoopService;
import java.sql.Date;
import java.util.List;
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
                    coopService.updateCoop(
                            coop, CoopStatus.IN_PROGRESS, null, null, null, null);
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
}
