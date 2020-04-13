package ca.mcgill.cooperator.dao;

import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Integer> {

    Set<CourseOffering> findByCourse(Course c);

    CourseOffering findByCourseAndYearAndSeason(Course c, int year, Season season);

    List<CourseOffering> findByYear(int year);

    List<CourseOffering> findBySeason(Season season);
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM course_offering_report_config WHERE course_offering_id = ?1", nativeQuery = true)
    int deleteAllReportConfigsById(int id);
}
