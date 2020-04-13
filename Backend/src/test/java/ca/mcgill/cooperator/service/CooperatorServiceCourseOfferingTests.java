package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.Season;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceCourseOfferingTests extends BaseServiceTest {

    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired ReportConfigService reportConfigService;

    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void testCreateCourseOffering() {
        String name = "ECSE321";
        int year = 2020;
        Season season = Season.WINTER;
        Course c = courseService.createCourse(name);

        try {
            courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }
        c = courseRepository.findByName(name);
        CourseOffering co = courseOfferingRepository.findByCourse(c).stream().findFirst().get();
        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        assertEquals(co.getCourse().getId(), c.getId());
        c = courseService.getCourseById(c.getId());
        assertEquals(season, c.getCourseOfferings().get(0).getSeason());
    }

    @Test
    public void testCreateCourseOfferingNull() {

        try {
            courseOfferingService.createCourseOffering(0, null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Year is invalid! Season cannot be null! Course cannot be null!",
                    e.getMessage());
        }

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 0);
    }

    @Test
    public void testUpdateCourseOffering() {
        String name = "ECSE321";
        int year = 2020;
        Season season = Season.WINTER;
        Course c = courseService.createCourse(name);

        try {
            courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }

        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        assertEquals(co.getCourse().getId(), c.getId());

        int year2 = 2021;
        Season season2 = Season.FALL;
        String name2 = "ECSE223";
        Course c2 = courseService.createCourse(name2);
        Set<CourseOffering> courseOfferings = new HashSet<CourseOffering>();
        courseOfferings.add(co);
        ReportConfig rc = createTestReportConfig(reportConfigService, "First Evaluation", courseOfferings);
        Set<ReportConfig> reportConfigs = new HashSet<ReportConfig>();
        reportConfigs.add(rc);

        try {
            courseOfferingService.updateCourseOffering(co, year2, season2, c2, null, reportConfigs);

        } catch (IllegalArgumentException e) {
            fail();
        }

        co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season2);
        assertEquals(co.getYear(), year2);
        assertEquals(co.getCourse().getId(), c2.getId());
        assertEquals(1, co.getReportConfigs().size());
        c2 = courseService.getCourseById(c2.getId());
        assertEquals(season2, c2.getCourseOfferings().get(0).getSeason());
    }

    @Test
    public void testUpdateCourseOfferingInvalid() {
        String name = "ECSE321";
        int year = 2020;
        Season season = Season.WINTER;
        Course c = courseService.createCourse(name);

        try {
            courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }

        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        assertEquals(co.getCourse().getId(), c.getId());

        int year2 = -1;
        Season season2 = null;
        Course c2 = null;

        try {
            courseOfferingService.updateCourseOffering(null, year2, season2, c2, null, null);

        } catch (IllegalArgumentException e) {
            assertEquals(
                    ERROR_PREFIX + "Course Offering to update cannot be null!", e.getMessage());
        }

        co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);
        assertEquals(co.getSeason(), season);
        assertEquals(co.getYear(), year);
        assertEquals(co.getCourse().getId(), c.getId());
    }

    @Test
    public void testDeleteCourseOffering() {
        String name = "ECSE321";
        int year = 2020;
        Season season = Season.WINTER;
        Course c = courseService.createCourse(name);

        try {
            courseOfferingService.createCourseOffering(year, season, c);

        } catch (IllegalArgumentException e) {
            fail();
        }

        CourseOffering co = courseOfferingService.getAllCourseOfferings().get(0);

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 1);

        try {
            courseOfferingService.deleteCourseOffering(co);
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals(courseOfferingService.getAllCourseOfferings().size(), 0);
    }
}
