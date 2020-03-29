package ca.mcgill.cooperator.schedule;

import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.service.CoopService;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class contains jobs that should run in the background on an automated basis. Job frequency
 * should be specified with a cron expression.
 */
@Component
public class NightlyTasks {

    @Autowired CoopService coopService;

    // runs once every weekday at 4am
    static final String CRON_WEEKDAY = "0 0 4 * * MON-FRI";

    // runs every 5 minutes, use this for testing
    static final String CRON_TEST = "0 */5 * ? * *";

    /** Checks all existing Coops and updates their status if necessary. */
    @Scheduled(cron = CRON_TEST)
    public void updateCoopStatuses() {
        List<Coop> coops = coopService.getAllCoops();

        Date currentDate = new Date(System.currentTimeMillis());
        Date coopDate;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        System.out.println(
                df.format(currentDate) + " [Schedule] Running job to update Coop statuses");

        for (Coop coop : coops) {
            if (coop.getStatus() == CoopStatus.FUTURE) {
                if (coop.getCoopDetails() == null) continue;

                coopDate = coop.getCoopDetails().getStartDate();
                // check if the co-op starts today or has already started
                if (currentDate.compareTo(coopDate) >= 0) {
                    // update status to IN_PROGRESS
                    coopService.updateCoop(
                            coop, CoopStatus.IN_PROGRESS, null, null, null, null, null);
                }
            } else if (coop.getStatus() == CoopStatus.IN_PROGRESS) {
                coopDate = coop.getCoopDetails().getEndDate();
                // check if the co-op ended today or has already ended
                if (currentDate.compareTo(coopDate) >= 0) {
                    // update status to FINISHED
                    coopService.updateCoop(coop, CoopStatus.FINISHED, null, null, null, null, null);
                }
            }
        }
    }
}
