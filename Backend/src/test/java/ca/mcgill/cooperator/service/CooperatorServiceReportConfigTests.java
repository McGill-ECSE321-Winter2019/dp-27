package ca.mcgill.cooperator.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.model.ReportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceReportConfigTests extends BaseServiceTest {

    @Autowired ReportConfigService reportConfigService;

    @Autowired ReportConfigRepository reportConfigRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reportConfigRepository.deleteAll();
    }

    @Test
    public void testCreateReportConfig() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";

        // 1. create valid ReportConfig
        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. verify correctness
        assertEquals(1, reportConfigService.getAllReportConfigs().size());

        ReportConfig rc = reportConfigService.getReportConfig("First Evaluation");
        assertEquals(14, rc.getDeadline());
        assertTrue(rc.getRequiresFile());
        assertTrue(rc.getIsDeadlineFromStart());
    }

    @Test
    public void testReportConfigUniqueType() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";

        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type);
            // type must be unique so expect a SQLException
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type);
        } catch (Exception e) {
            assertEquals(1, reportConfigService.getAllReportConfigs().size());
        }
    }

    @Test
    public void testCreateReportConfigInvalid() {
        try {
            reportConfigService.createReportConfig(true, -1, false, "  ");
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Deadline cannot be negative! " + "Report type cannot be empty!",
                    e.getMessage());
            assertEquals(0, reportConfigService.getAllReportConfigs().size());
        }
    }

    @Test
    public void testUpdateReportConfig() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";

        // 1. create the ReportConfig
        ReportConfig rc = null;
        try {
            rc =
                    reportConfigService.createReportConfig(
                            requiresFile, deadline, isDeadlineFromStart, type);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. update the type
        try {
            reportConfigService.updateReportConfig(rc, null, null, null, "Second Evaluation", null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 3. try fetching by type
        rc = reportConfigService.getReportConfig("Second Evaluation");
        assertEquals(14, rc.getDeadline());
        assertTrue(rc.getRequiresFile());
        assertTrue(rc.getIsDeadlineFromStart());

        // 4. update all fields
        requiresFile = false;
        deadline = 7;
        isDeadlineFromStart = false;
        type = "Third Evaluation";
        try {
            reportConfigService.updateReportConfig(
                    rc, requiresFile, deadline, isDeadlineFromStart, type, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 5. verify changes
        rc = reportConfigService.getReportConfig(type);
        assertEquals(deadline, rc.getDeadline());
        assertFalse(rc.getRequiresFile());
        assertFalse(rc.getIsDeadlineFromStart());
    }

    @Test
    public void testUpdateReportConfigInvalid() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";

        // 1. create the ReportConfig
        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. invalid updates
        try {
            reportConfigService.updateReportConfig(null, null, -1, null, "", null);
        } catch (IllegalArgumentException e) {
            // 3. verify original ReportConfig is unchanged
            assertEquals(
                    ERROR_PREFIX
                            + "Report config to update cannot be null! "
                            + "Deadline cannot be negative! "
                            + "Report type cannot be empty!",
                    e.getMessage());
            assertEquals(1, reportConfigService.getAllReportConfigs().size());
        }

        ReportConfig rc = reportConfigService.getReportConfig("First Evaluation");
        assertEquals(14, rc.getDeadline());
        assertTrue(rc.getRequiresFile());
        assertTrue(rc.getIsDeadlineFromStart());
    }

    @Test
    public void testDeleteReportConfig() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";

        // 1. create then delete the ReportConfig
        try {
            ReportConfig rc =
                    reportConfigService.createReportConfig(
                            requiresFile, deadline, isDeadlineFromStart, type);

            reportConfigService.deleteReportConfig(rc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(0, reportConfigService.getAllReportConfigs().size());
    }

    @Test
    public void testDeleteReportConfigInvalid() {
        try {
            reportConfigService.deleteReportConfig(null);
        } catch (IllegalArgumentException e) {
            assertEquals(ERROR_PREFIX + "Report config to delete cannot be null!", e.getMessage());
            assertEquals(0, reportConfigService.getAllReportConfigs().size());
        }
    }
}
