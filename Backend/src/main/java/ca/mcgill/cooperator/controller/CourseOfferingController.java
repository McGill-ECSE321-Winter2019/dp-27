package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.model.CourseOffering;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("course-offerings")
public class CourseOfferingController {

    @GetMapping("/{id}")
    public String getCourseOfferingById(@PathVariable int id) {
        return "Hello World";
    }

    private CourseOfferingDto convertToDto(CourseOffering co) {
        if (co == null) {
            throw new IllegalArgumentException("Course Offering does not exist!");
        }
        return new CourseOfferingDto(
                co.getId(), co.getYear(), co.getSeason(), co.getCourse(), co.getCoops());
    }
}
