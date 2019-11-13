package ca.mcgill.cooperator.controller;

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
}
