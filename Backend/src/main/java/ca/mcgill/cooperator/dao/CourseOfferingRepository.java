package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import java.util.List;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Integer> {

    Set<CourseOffering> findByCourse(Course c);

    CourseOffering findByCourseAndYearAndSeason(Course c, int year, Season season);

    List<CourseOffering> findByYear(int year);

    List<CourseOffering> findBySeason(Season season);
}
