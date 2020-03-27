package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.EmployerReportService;
import ca.mcgill.cooperator.service.StudentReportService;
import ca.mcgill.cooperator.service.StudentService;
import java.util.HashSet;
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
    @Autowired EmployerReportService employerReportService;
    @Autowired StudentReportService studentReportService;

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
        Student student = studentService.getStudentById(studentDto.getId());

        CourseOfferingDto courseOfferingDto = coopDto.getCourseOffering();

        CourseOffering courseOffering;
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

    @PutMapping("")
    public CoopDto updateCoop(@RequestBody CoopDto coopDto) {
        Coop coop = coopService.getCoopById(coopDto.getId());

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

        Set<EmployerReport> employerReports = null;
        if (coopDto.getEmployerReports() != null) {
            List<EmployerReportDto> employerReportDtos = coopDto.getEmployerReports();
            employerReports = new HashSet<EmployerReport>();

            for (EmployerReportDto employerReportDto : employerReportDtos) {
                EmployerReport employerReport =
                        employerReportService.getEmployerReport(employerReportDto.getId());
                employerReports.add(employerReport);
            }
        }

        Set<StudentReport> studentReports = null;
        if (coopDto.getStudentReports() != null) {
            List<StudentReportDto> studentReportDtos = coopDto.getStudentReports();
            studentReports = new HashSet<StudentReport>();

            for (StudentReportDto studentReportDto : studentReportDtos) {
                StudentReport studentReport =
                        studentReportService.getStudentReport(studentReportDto.getId());
                studentReports.add(studentReport);
            }
        }

        coop =
                coopService.updateCoop(
                        coop,
                        coopDto.getStatus(),
                        courseOffering,
                        student,
                        coopDetails,
                        employerReports,
                        studentReports);
        return ControllerUtils.convertToDto(coop);
    }

    @DeleteMapping("/{id}")
    public CoopDto deleteCoop(@PathVariable int id) {
        Coop coop = coopService.getCoopById(id);

        return ControllerUtils.convertToDto(coopService.deleteCoop(coop));
    }
}
