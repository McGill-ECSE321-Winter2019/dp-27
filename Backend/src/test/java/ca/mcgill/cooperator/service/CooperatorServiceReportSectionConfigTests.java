package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.ReportSectionConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CourseRepository courseRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	courseOfferingRepository.deleteAll();
    	courseRepository.deleteAll();
        reportConfigRepository.deleteAll();
        reportSectionConfigRepository.deleteAll();
    }

    @Test
    public void testCreateReportSectionConfig() {
        String prompt = "How was your co-op?";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create valid ReportSectionConfig
        try {
            ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
            reportSectionConfigService.createReportSectionConfig(
                    prompt, ReportResponseType.LONG_TEXT, 1, rc);
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
            reportSectionConfigService.createReportSectionConfig("  ", null, -1, null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX
                            + "Section prompt cannot be empty! "
                            + "Response type cannot be null! "
                            + "Invalid question number! "
                            + "Report config cannot be null!",
                    e.getMessage());
        }
    }

    @Test
    public void testUpdateReportSectionConfig() {
        String prompt = "How was your co-op?";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create ReportSectionConfig
        ReportSectionConfig rsConfig = null;
        ReportConfig rc = null;
        try {
            rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
            rsConfig =
                    reportSectionConfigService.createReportSectionConfig(
                            prompt, ReportResponseType.LONG_TEXT, 1, rc);
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
                            rc, null, null, null, "Second Evaluation", null, courseOfferings, null);

            reportSectionConfigService.updateReportSectionConfig(
                    rsConfig, prompt, ReportResponseType.SHORT_TEXT, 2, null, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 5. verify correctness
        rsConfig = reportSectionConfigService.getReportSectionConfig(rsConfig.getId());
        rc = reportConfigService.getReportConfig(rc.getId());
        assertEquals(prompt, rsConfig.getSectionPrompt());
        assertEquals(ReportResponseType.SHORT_TEXT, rsConfig.getResponseType());
        assertEquals(rc.getType(), rsConfig.getReportConfig().getType());
        assertEquals(1, rc.getCourseOfferings().size());
    }

    @Test
    public void testUpdateReportSectionConfigInvalid() {
        String prompt = "How was your co-op?";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create ReportSectionConfig
        try {
            ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
            reportSectionConfigService.createReportSectionConfig(
                    prompt, ReportResponseType.LONG_TEXT, 1, rc);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. invalid updates
        try {
            reportSectionConfigService.updateReportSectionConfig(null, " ", null, -1, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX
                            + "Report section config to update cannot be null! "
                            + "Section prompt cannot be empty! "
                            + "Question number cannot be less than 1!",
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
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create and delete ReportSectionConfig
        try {
            ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
            ReportSectionConfig rsc =
                    reportSectionConfigService.createReportSectionConfig(
                            prompt, ReportResponseType.LONG_TEXT, 1, rc);

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
