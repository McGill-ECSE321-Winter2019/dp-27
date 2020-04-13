package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportConfig;
import ca.mcgill.cooperator.model.Season;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseOfferingService extends BaseService {

    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ReportConfigRepository reportConfigRepository;

    /**
     * Creates a new CourseOffering
     *
     * @param year
     * @param season
     * @param course
     * @return the created CourseOffering
     */
    @Transactional
    public CourseOffering createCourseOffering(int year, Season season, Course course) {
        StringBuilder error = new StringBuilder();
        if (year <= 0) {
            error.append("Year is invalid! ");
        }
        if (season == null) {
            error.append("Season cannot be null! ");
        }
        if (course == null) {
            error.append("Course cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        CourseOffering co = new CourseOffering();
        co.setYear(year);
        co.setSeason(season);
        co.setCourse(course);
        co.setCoops(new HashSet<Coop>());
        co.setReportConfigs(new HashSet<ReportConfig>());

        return courseOfferingRepository.save(co);
    }

    /**
     * Gets a CourseOffering by ID
     *
     * @param id
     * @return CourseOffering with specified ID
     */
    @Transactional
    public CourseOffering getCourseOfferingById(int id) {
        CourseOffering c = courseOfferingRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course Offering with ID " + id + " does not exist!");
        }
        return c;
    }

    /**
     * Gets all CourseOfferings for the specified year
     *
     * @param year
     * @return all CourseOfferings for the specified year
     */
    @Transactional
    public Set<CourseOffering> getCourseOfferings(int year) {
        return ServiceUtils.toSet(courseOfferingRepository.findByYear(year));
    }

    /**
     * Gets all CourseOfferings for the specified season
     *
     * @param season
     * @return all CourseOfferings for the specified season
     */
    @Transactional
    public Set<CourseOffering> getCourseOfferings(Season season) {
        return ServiceUtils.toSet(courseOfferingRepository.findBySeason(season));
    }

    /**
     * Gets all CourseOfferings for the specified Course
     *
     * @param course
     * @return all CourseOfferings for the specified Course
     */
    @Transactional
    public Set<CourseOffering> getCourseOfferingsByCourse(Course course) {
        Set<CourseOffering> co = courseOfferingRepository.findByCourse(course);
        if (co == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX
                            + "There are no course offerings for the course "
                            + course.getName()
                            + "!");
        }
        return co;
    }

    /**
     * Gets the CourseOffering for the specified Course and term
     *
     * @param course
     * @param year
     * @param season
     * @return CourseOffering for the specified Course and term
     */
    @Transactional
    public CourseOffering getCourseOfferingByCourseAndTerm(Course course, int year, Season season) {
        CourseOffering co =
                courseOfferingRepository.findByCourseAndYearAndSeason(course, year, season);
        // no need to throw an exception, just return null
        return co;
    }

    /**
     * Gets all CourseOfferings
     *
     * @return all CourseOfferings
     */
    @Transactional
    public List<CourseOffering> getAllCourseOfferings() {
        List<CourseOffering> co = ServiceUtils.toList(courseOfferingRepository.findAll());
        if (co == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "There are no course offerings!");
        }
        return co;
    }

    /**
     * Gets all CourseOfferings as a Set
     *
     * @return all CourseOfferings in a Set
     */
    @Transactional
    public Set<CourseOffering> getAllCourseOfferingsSet() {
        Set<CourseOffering> co = ServiceUtils.toSet(courseOfferingRepository.findAll());
        if (co == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "There are no course offerings!");
        }
        return co;
    }

    /**
     * Updates an existing CourseOffering
     *
     * @param courseOffering
     * @param year
     * @param season
     * @param course
     * @return the updated CourseOffering
     */
    @Transactional
    public CourseOffering updateCourseOffering(
            CourseOffering courseOffering, int year, Season season, Course course, Set<Coop> coops, Set<ReportConfig> reportConfigs) {
        StringBuilder error = new StringBuilder();
        if (courseOffering == null) {
            error.append("Course Offering to update cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (year > 0) {
            courseOffering.setYear(year);
        }
        if (season != null) {
            courseOffering.setSeason(season);
        }
        if (course != null) {
            courseOffering.setCourse(course);
        }
        if (coops != null) {
        	courseOffering.setCoops(coops);
        }
        if (reportConfigs != null) {
        	courseOfferingRepository.deleteAllReportConfigsById(courseOffering.getId());
        	courseOffering.setReportConfigs(reportConfigs);
        }

        return courseOfferingRepository.save(courseOffering);
    }

    /**
     * Deletes an existing CourseOffering
     *
     * @param courseOffering
     * @return the deleted CourseOffering
     */
    @Transactional
    public CourseOffering deleteCourseOffering(CourseOffering courseOffering) {
        if (courseOffering == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course offering to delete cannot be null!");
        }
        courseOfferingRepository.delete(courseOffering);

        Course c = courseOffering.getCourse();
        List<CourseOffering> courseOfferings = c.getCourseOfferings();
        courseOfferings.remove(courseOffering);
        c.setCourseOfferings(courseOfferings);
        courseRepository.save(c);

        return courseOffering;
    }
}
