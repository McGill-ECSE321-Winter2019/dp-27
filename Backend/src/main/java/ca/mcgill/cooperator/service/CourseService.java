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
     * Creates a new Course
     *
     * @param name
     * @return the created Course
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

    /**
     * Gets an existing Course by name
     *
     * @param name
     * @return Course with specified name
     */
    @Transactional
    public Course getCourseByName(String name) {
        Course c = null;
        try {
            c = courseRepository.findByName(name.trim());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    ERROR_PREFIX + "Course with name " + name + " does not exist!");
        }
        return c;
    }

    /**
     * Gets the names of all Courses
     *
     * @return list of all Course names
     */
    @Transactional
    public List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        ServiceUtils.toList(courseRepository.findAll()).stream()
                .forEach(c -> courseNames.add(c.getName()));
        ;
        return courseNames;
    }

    /**
     * Gets an existing Course by ID
     *
     * @param id
     * @return Course with specified ID
     */
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

    /**
     * Updates an existing Course
     *
     * @param course
     * @param name
     * @param offerings
     * @return the updated Course
     */
    @Transactional
    public Course updateCourse(Course course, String name, List<CourseOffering> offerings) {
        StringBuilder error = new StringBuilder();
        if (course == null) {
            error.append("Course to update cannot be null! ");
        }
        if (name != null && name.trim().length() == 0) {
            error.append("Course name cannot be empty!");
        }
        if (error.length() > 0) {
            throw new IllegalArgumentException(ERROR_PREFIX + error.toString().trim());
        }

        if (offerings != null) {
            course.setCourseOfferings(offerings);
        }
        if (name != null) {
            course.setName(name.trim());
        }

        return courseRepository.save(course);
    }

    /**
     * Deletes an existing Course
     *
     * @param course
     * @return the deleted Course
     */
    @Transactional
    public Course deleteCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException(ERROR_PREFIX + "Course to delete cannot be null!");
        }
        courseRepository.delete(course);
        return course;
    }
}
