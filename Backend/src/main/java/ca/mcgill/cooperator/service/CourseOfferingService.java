package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseOfferingService extends BaseService {
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
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        CourseOffering co = new CourseOffering();
        co.setYear(year);
        co.setSeason(season);
        co.setCourse(c);
        co.setCoops(new ArrayList<Coop>());

        return courseOfferingRepository.save(co);
    }

    @Transactional
    public CourseOffering updateCourseOffering(
            CourseOffering co, int year, Season season, Course c) {
        StringBuilder error = new StringBuilder();
        if (co == null) {
            error.append("Course Offering to update cannot be null!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (year > 0) {
            co.setYear(year);
        }
        if (season != null) {
            co.setSeason(season);
        }
        if (c != null) {
            co.setCourse(c);
        }

        return courseOfferingRepository.save(co);
    }

    @Transactional
    public CourseOffering getCourseOfferingById(int id) {
        CourseOffering c = courseOfferingRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course Offering with ID " + id + " does not exist!");
        }
        return c;
    }
    
    @Transactional
    public Set<CourseOffering> getCourseOfferings(int year){
    	return ServiceUtils.toSet(courseOfferingRepository.findByYear(year));
    }
    
    @Transactional
    public Set<CourseOffering> getCourseOfferings(Season season){
    	return ServiceUtils.toSet(courseOfferingRepository.findBySeason(season));
    }
   

    @Transactional
    public Set<CourseOffering> getCourseOfferingsByCourse(Course c) {
        Set<CourseOffering> co = courseOfferingRepository.findByCourse(c);
        if (co == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX
                            + "There are no course offerings for the course "
                            + c.getName()
                            + "!");
        }
        return co;
    }

    @Transactional
    public CourseOffering getCourseOfferingByCourseAndTerm(Course c, int year, Season season) {
        CourseOffering co = courseOfferingRepository.findByCourseAndYearAndSeason(c, year, season);
        // no need to throw an exception, just return null
        return co;
    }

    @Transactional
    public List<CourseOffering> getAllCourseOfferings() {
        List<CourseOffering> co = ServiceUtils.toList(courseOfferingRepository.findAll());
        if (co == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "There are no course offerings!");
        }
        return co;
    }
    
    @Transactional
    public Set<CourseOffering> getAllCourseOfferingsSet() {
        Set<CourseOffering> co = ServiceUtils.toSet(courseOfferingRepository.findAll());
        if (co == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "There are no course offerings!");
        }
        return co;
    }

    @Transactional
    public CourseOffering deleteCourseOffering(CourseOffering co) {
        if (co == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course offering to delete cannot be null!");
        }
        courseOfferingRepository.delete(co);

        Course c = co.getCourse();
        List<CourseOffering> cos = c.getCourseOfferings();
        cos.remove(co);
        c.setCourseOfferings(cos);
        courseRepository.save(c);

        return co;
    }
}
