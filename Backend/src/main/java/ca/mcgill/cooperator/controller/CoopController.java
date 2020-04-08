package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.ReportService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("coops")
public class CoopController extends BaseController {

    @Autowired CoopService coopService;
    @Autowired CourseService courseService;
    @Autowired CourseOfferingService courseOfferingService;
    @Autowired StudentService studentService;
    @Autowired CoopDetailsService coopDetailsService;
    @Autowired ReportService reportService;

    @GetMapping("/{id}")
    public CoopDto getCoopById(@PathVariable int id) {
        Coop coop = coopService.getCoopById(id);
        return ControllerUtils.convertToDto(coop);
    }

    @GetMapping("")
    public List<CoopDto> getAllCoops(@RequestParam(required = false) String status) {
        List<Coop> coops;
        if (status == null) {
            coops = coopService.getAllCoops();
        } else {
            coops = coopService.getCoopsByStatus(CoopStatus.valueOf(status));
        }
        return ControllerUtils.convertCoopListToDto(coops);
    }

    @GetMapping("/status")
    public CoopStatus[] getAllCoopStatuses() {
        CoopStatus[] status = CoopStatus.values();
        return status;
    }

    @GetMapping("/student/{id}")
    public List<CoopDto> getCoopByStudentId(@PathVariable int id) {
        Student s = studentService.getStudentById(id);
        List<Coop> coops = coopService.getAllCoopsByStudent(s);
        return ControllerUtils.convertCoopListToDto(coops);
    }

    @PostMapping("")
    public CoopDto createCoop(@RequestBody CoopDto coopDto) {
        Coop coop = new Coop();

        StudentDto studentDto = coopDto.getStudent();
        Student student = null;
        if (coopDto.getStudent() != null) {
            student = studentService.getStudentById(studentDto.getId());
        }

        CourseOfferingDto courseOfferingDto = coopDto.getCourseOffering();

        CourseOffering courseOffering = null;
        if (courseOfferingDto.getId() > 0) {
            courseOffering = courseOfferingService.getCourseOfferingById(courseOfferingDto.getId());
        } else {
            // if no ID present, get CourseOffering via Course
            String courseName = courseOfferingDto.getCourse().getName();
            Course course = courseService.getCourseByName(courseName);

            courseOffering =
                    courseOfferingService.getCourseOfferingByCourseAndTerm(
                            course, courseOfferingDto.getYear(), courseOfferingDto.getSeason());

            if (courseOffering == null) {
                // create the CourseOffering if it doesn't already exist
                courseOffering =
                        courseOfferingService.createCourseOffering(
                                courseOfferingDto.getYear(), courseOfferingDto.getSeason(), course);
            }
        }

        coop = coopService.createCoop(coopDto.getStatus(), courseOffering, student);

        return ControllerUtils.convertToDto(coop);
    }

    @PutMapping("/{id}")
    public CoopDto updateCoop(@PathVariable int id, @RequestBody CoopDto coopDto) {
        Coop coop = coopService.getCoopById(id);

        // set everything to null initially so that we don't update fields that aren't present
        CourseOffering courseOffering = null;
        if (coopDto.getCourseOffering() != null) {
            courseOffering =
                    courseOfferingService.getCourseOfferingById(
                            coopDto.getCourseOffering().getId());
        }

        Student student = null;
        if (coopDto.getStudent() != null) {
            student = studentService.getStudentById(coopDto.getStudent().getId());
        }

        CoopDetails coopDetails = null;
        if (coopDto.getCoopDetails() != null) {
            coopDetails = coopDetailsService.getCoopDetails(coopDto.getCoopDetails().getId());
        }

        Set<Report> reports = null;
        if (coopDto.getReports() != null) {
            List<ReportDto> reportDtos = coopDto.getReports();
            reports =
                    ControllerUtils.convertReportDtosToDomainObjects(
                            reportService, reportDtos);
        }

        coop =
                coopService.updateCoop(
                        coop,
                        coopDto.getStatus(),
                        courseOffering,
                        student,
                        coopDetails,
                        reports);
        return ControllerUtils.convertToDto(coop);
    }

    @DeleteMapping("/{id}")
    public CoopDto deleteCoop(@PathVariable int id) {
        Coop coop = coopService.getCoopById(id);

        return ControllerUtils.convertToDto(coopService.deleteCoop(coop));
    }
}
