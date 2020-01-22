package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseOfferingService {
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CourseRepository courseRepository;

    /**
     * create new course offering in database
     *
     * @param year
     * @param season
     * @param c
     * @return created course offering
     */
    @Transactional
    public CourseOffering createCourseOffering(int year, Season season, Course c) {
        StringBuilder error = new StringBuilder();
        if (year <= 0) {
            error.append("Year is invalid! ");
        }
        if (season == null) {
            error.append("Season cannot be null! ");
        }
        if (c == null) {
            error.append("Course cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        CourseOffering co = new CourseOffering();
        co.setYear(year);
        co.setSeason(season);
        co.setCourse(c);
        co.setCoops(new ArrayList<Coop>());

        courseOfferingRepository.save(co);

        List<CourseOffering> offerings = c.getCourseOfferings();
        offerings.add(co);
        c.setCourseOfferings(offerings);

        courseRepository.save(c);

        return courseOfferingRepository.save(co);
    }

    @Transactional
    public CourseOffering updateCourseOffering(
            CourseOffering co, int year, Season season, Course c) {
        StringBuilder error = new StringBuilder();
        if (year <= 0) {
            error.append("Year is invalid! ");
        }
        if (season == null) {
            error.append("Season cannot be null! ");
        }
        if (c == null) {
            error.append("Course cannot be null! ");
        }
        if (co == null) {
            error.append("Course Offering cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(error.toString().trim());
        }

        co.setYear(year);
        co.setSeason(season);
        co.setCourse(c);
        co.setCoops(new ArrayList<Coop>());

        courseOfferingRepository.save(co);

        List<CourseOffering> offerings = c.getCourseOfferings();
        offerings.add(co);
        c.setCourseOfferings(offerings);

        courseRepository.save(c);

        return courseOfferingRepository.save(co);
    }

    @Transactional
    public CourseOffering getCourseOfferingById(int id) {
        CourseOffering c = courseOfferingRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException(
                    "Course Offering with ID " + id + " does not exist!");
        }
        return c;
    }

    @Transactional
    public List<CourseOffering> getCourseOfferingsByCourse(Course c) {
        List<CourseOffering> co = courseOfferingRepository.findByCourse(c);
        if (co == null) {
            throw new IllegalArgumentException(
                    "There are no course offerings for the course " + c.getName() + "!");
        }
        return co;
    }

    @Transactional
    public List<CourseOffering> getAllCourseOfferings() {
        List<CourseOffering> co = ServiceUtils.toList(courseOfferingRepository.findAll());
        if (co == null) {
            throw new IllegalArgumentException("There are no course offerings!");
        }
        return co;
    }

    @Transactional
    public CourseOffering deleteCourseOffering(CourseOffering co) {
        courseOfferingRepository.delete(co);
        Course c = co.getCourse();
        List<CourseOffering> cos = c.getCourseOfferings();
        cos.remove(co);
        c.setCourseOfferings(cos);
        courseRepository.save(c);
        return co;
    }
}
