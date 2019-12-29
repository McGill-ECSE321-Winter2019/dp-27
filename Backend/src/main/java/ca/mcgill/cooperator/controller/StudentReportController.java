package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.StudentReport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("student-reports")
public class StudentReportController {

    @GetMapping("/{id}")
    public String getStudentReportById(@PathVariable int id) {
        return "Hello World";
    }

    private StudentReportDto convertToDto(StudentReport sr) {
        if (sr == null) {
            throw new IllegalArgumentException("Student Report does not exist!");
        }
        return new StudentReportDto(
                sr.getId(), sr.getStatus(), sr.getCoop(), sr.getReportSections());
    }
}
