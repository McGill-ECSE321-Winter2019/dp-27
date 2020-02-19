package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Integer> {

    List<CourseOffering> findByCourse(Course c);

    CourseOffering findByCourseAndYearAndSeason(Course c, int year, Season season);
}
