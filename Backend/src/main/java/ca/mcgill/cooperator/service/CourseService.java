package ca.mcgill.cooperator.service;

import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService extends BaseService {
    @Autowired CourseRepository courseRepository;

    /**
     * create new course in database
     *
     * @param name
     * @return created course
     */
    @Transactional
    public Course createCourse(String name) {
        StringBuilder error = new StringBuilder();
        if (name == null || name.trim().length() == 0) {
            error.append("Course name cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        Course c = new Course();
        c.setName(name.trim());
        c.setCourseOfferings(new ArrayList<CourseOffering>());

        return courseRepository.save(c);
    }

    @Transactional
    public Course updateCourse(Course c, String name, List<CourseOffering> offerings) {
        StringBuilder error = new StringBuilder();
        if (c == null) {
            error.append("Course to update cannot be null! ");
        }
        if (name != null && name.trim().length() == 0) {
            error.append("Course name cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (offerings != null) {
            c.setCourseOfferings(offerings);
        }
        if (name != null) {
            c.setName(name.trim());
        }

        return courseRepository.save(c);
    }

    @Transactional
    public Course getCourseByName(String name) {
        Course c = courseRepository.findByName(name.trim());
        if (c == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course with name " + name + " does not exist!");
        }
        return c;
    }

    @Transactional
    public Course getCourseById(int id) {
        Course c = courseRepository.findById(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course with ID " + id + " does not exist!");
        }
        return c;
    }

    @Transactional
    public List<Course> getAllCourses() {
        return ServiceUtils.toList(courseRepository.findAll());
    }

    @Transactional
    public Course deleteCourse(Course c) {
        if (c == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Course to delete cannot be null!");
        }
        courseRepository.delete(c);
        return c;
    }
}
