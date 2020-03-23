package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSectionConfig;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceReportSectionConfigTests extends BaseServiceTest {

    @Autowired ReportConfigService reportConfigService;
    @Autowired ReportSectionConfigService reportSectionConfigService;

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reportConfigRepository.deleteAll();
        reportSectionConfigRepository.deleteAll();
    }

    @Test
    public void testCreateReportSectionConfig() {
        String prompt = "How was your co-op?";

        // 1. create valid ReportSectionConfig
        try {
            ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
            reportSectionConfigService.createReportSectionConfig(
                    prompt, ReportResponseType.LONG_TEXT, rc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. verify correctness
        List<ReportSectionConfig> rsConfigs =
                reportSectionConfigService.getAllReportSectionConfigs();
        assertEquals(1, rsConfigs.size());
        assertEquals(prompt, rsConfigs.get(0).getSectionPrompt());
        assertEquals(ReportResponseType.LONG_TEXT, rsConfigs.get(0).getResponseType());
    }

    @Test
    public void testCreateReportSectionConfigInvalid() {
        try {
            reportSectionConfigService.createReportSectionConfig("  ", null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX
                            + "Section prompt cannot be empty! "
                            + "Response type cannot be null! "
                            + "Report config cannot be null!",
                    e.getMessage());
        }
    }

    @Test
    public void testUpdateReportSectionConfig() {
        String prompt = "How was your co-op?";

        // 1. create ReportSectionConfig
        ReportSectionConfig rsConfig = null;
        ReportConfig rc = null;
        try {
        	rc = createTestReportConfig(reportConfigService, "First Evaluation");
            rsConfig =
                    reportSectionConfigService.createReportSectionConfig(
                            prompt, ReportResponseType.LONG_TEXT, rc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. update prompt only
        prompt = "Which tools did you use during your co-op?";
        try {
            reportSectionConfigService.updateReportSectionConfig(
                    rsConfig, prompt, null, null, null, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 3. verify that only the prompt was changed
        rsConfig = reportSectionConfigService.getReportSectionConfig(rsConfig.getId());
        assertEquals(prompt, rsConfig.getSectionPrompt());
        assertEquals(ReportResponseType.LONG_TEXT, rsConfig.getResponseType());
        assertEquals(rc.getType(), rsConfig.getReportConfig().getType());

        // 4. update all fields
        prompt = "How was your co-op?";
        try {
            // update ReportConfig here without explicitly updating it below
            rc =
                    reportConfigService.updateReportConfig(
                            rc, null, null, null, "Second Evaluation", null);

            reportSectionConfigService.updateReportSectionConfig(
                    rsConfig, prompt, ReportResponseType.SHORT_TEXT, null, null, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 5. verify correctness
        rsConfig = reportSectionConfigService.getReportSectionConfig(rsConfig.getId());
        assertEquals(prompt, rsConfig.getSectionPrompt());
        assertEquals(ReportResponseType.SHORT_TEXT, rsConfig.getResponseType());
        assertEquals(rc.getType(), rsConfig.getReportConfig().getType());
    }

    @Test
    public void testUpdateReportSectionConfigInvalid() {
        String prompt = "How was your co-op?";

        // 1. create ReportSectionConfig
        try {
        	ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
            reportSectionConfigService.createReportSectionConfig(
                    prompt, ReportResponseType.LONG_TEXT, rc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. invalid updates
        try {
            reportSectionConfigService.updateReportSectionConfig(null, " ", null, null, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX
                            + "Report section config to update cannot be null! "
                            + "Section prompt cannot be empty!",
                    e.getMessage());
        }

        // 3. verify original ReportSectionConfig is unchanged
        List<ReportSectionConfig> rsConfigs =
                reportSectionConfigService.getAllReportSectionConfigs();
        assertEquals(1, rsConfigs.size());
        assertEquals(prompt, rsConfigs.get(0).getSectionPrompt());
        assertEquals(ReportResponseType.LONG_TEXT, rsConfigs.get(0).getResponseType());
    }

    @Test
    public void testDeleteReportSectionConfig() {
        String prompt = "How was your co-op?";

        // 1. create and delete ReportSectionConfig
        try {
        	ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation");
            ReportSectionConfig rsc =
                    reportSectionConfigService.createReportSectionConfig(
                            prompt, ReportResponseType.LONG_TEXT, rc);

            reportSectionConfigService.deleteReportSectionConfig(rsc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        List<ReportSectionConfig> rsConfigs =
                reportSectionConfigService.getAllReportSectionConfigs();
        assertEquals(0, rsConfigs.size());
    }

    @Test
    public void testDeleteReportSectionConfigInvalid() {
        try {
            reportSectionConfigService.deleteReportSectionConfig(null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Report section config to delete cannot be null!",
                    e.getMessage());
        }

        List<ReportSectionConfig> rsConfigs =
                reportSectionConfigService.getAllReportSectionConfigs();
        assertEquals(0, rsConfigs.size());
    }
}
