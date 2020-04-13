package ca.mcgill.cooperator.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.Student;

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
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired ReportService reportService;
    @Autowired StudentService studentService;
    @Autowired CoopService coopService;

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired ReportRepository reportRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CoopRepository coopRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    	for (Report report : reportRepository.findAll()) {
    		report.setReportConfig(null);
    		reportRepository.save(report);
    	}
    	reportRepository.deleteAll();
        reportConfigRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        coopRepository.deleteAll();
        
    }

    @Test
    public void testCreateReportConfig() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create valid ReportConfig
        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);
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
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);


        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);
            // type must be unique so expect a SQLException
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);
        } catch (Exception e) {
            assertEquals(1, reportConfigService.getAllReportConfigs().size());
        }
    }

    @Test
    public void testCreateReportConfigInvalid() {
        try {
            reportConfigService.createReportConfig(true, -1, false, "  ", null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX 
                    + "Deadline cannot be negative! " 
                    + "Report type cannot be empty! "
                    + "Course Offerings cannot be null or empty!",
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
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Student student = createTestStudent(studentService);
        Coop coop = createTestCoop(coopService, courseOffering, student);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);


        // 1. create the ReportConfig
        ReportConfig rc = null;
        try {
            rc =
                    reportConfigService.createReportConfig(
                            requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);
        } catch (IllegalArgumentException e) {
            fail();
        }
        
        rc = reportConfigService.getReportConfig(rc.getId());
        courseOffering = courseOfferingService.getCourseOfferingById(courseOffering.getId());
        assertEquals(1, courseOffering.getReportConfigs().size());
        assertEquals(1, rc.getCourseOfferings().size());


        // 2. update the type
        try {
            reportConfigService.updateReportConfig(rc, null, null, null, "Second Evaluation", null, null, null);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 3. try fetching by type
        rc = reportConfigService.getReportConfig("Second Evaluation");
        assertEquals(14, rc.getDeadline());
        assertTrue(rc.getRequiresFile());
        assertTrue(rc.getIsDeadlineFromStart());

        // 4. update all fields
        Set<Report> reports = new HashSet<Report>();
        Report report = createTestStudentReport(reportService, coop, student, rc);
        reports.add(report);
        requiresFile = false;
        deadline = 7;
        isDeadlineFromStart = false;
        type = "Third Evaluation";
        try {
            reportConfigService.updateReportConfig(
                    rc, requiresFile, deadline, isDeadlineFromStart, type, null, null, reports);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 5. verify changes
        rc = reportConfigService.getReportConfig(type);
        assertEquals(deadline, rc.getDeadline());
        assertFalse(rc.getRequiresFile());
        assertFalse(rc.getIsDeadlineFromStart());
        assertEquals(1, rc.getReports().size());
        assertEquals(1, rc.getCourseOfferings().size());
    }

    @Test
    public void testUpdateReportConfigInvalid() {
        boolean requiresFile = true;
        int deadline = 14;
        boolean isDeadlineFromStart = true;
        String type = "First Evaluation";
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create the ReportConfig
        try {
            reportConfigService.createReportConfig(
                    requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);
        } catch (IllegalArgumentException e) {
            fail();
        }

        // 2. invalid updates
        try {
            reportConfigService.updateReportConfig(null, null, -1, null, "", null, null, null);
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
        Course course = createTestCourse(courseService);
        CourseOffering courseOffering = createTestCourseOffering(courseOfferingService, course);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(courseOffering);

        // 1. create then delete the ReportConfig
        try {
            ReportConfig rc =
                    reportConfigService.createReportConfig(
                            requiresFile, deadline, isDeadlineFromStart, type, courseOfferings);

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
