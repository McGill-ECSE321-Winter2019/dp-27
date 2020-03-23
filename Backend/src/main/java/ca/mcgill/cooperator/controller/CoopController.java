package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopDetails;
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
    public List<CoopDto> getAllCoops() {
        List<Coop> coops = coopService.getAllCoops();
        return ControllerUtils.convertCoopListToDto(coops);
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
            // we need to derive the Course Offering if ID is not given

            // this is a bit hacky, but if they have 0 co-ops already (for example) then this
            // is their first co-op term and they should be taking FACC 200
            String courseName = "FACC 20" + student.getCoops().size();
            // all courses (FACC 200-205) should always exist
            Course course = courseService.getCourseByName(courseName);

            courseOffering =
                    courseOfferingService.getCourseOfferingByCourseAndTerm(
                            course, courseOfferingDto.getYear(), courseOfferingDto.getSeason());

            if (courseOffering == null) {
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

        CourseOfferingDto courseOfferingDto = coopDto.getCourseOffering();
        CourseOffering courseOffering =
                courseOfferingService.getCourseOfferingById(courseOfferingDto.getId());

        StudentDto studentDto = coopDto.getStudent();
        Student student = studentService.getStudentById(studentDto.getId());

        CoopDetailsDto coopDetailsDto = coopDto.getCoopDetails();
        CoopDetails coopDetails = coopDetailsService.getCoopDetails(coopDetailsDto.getId());

        List<EmployerReportDto> employerReportDtos = coopDto.getEmployerReports();
        Set<EmployerReport> employerReports = new HashSet<EmployerReport>();

        if (employerReportDtos != null) {
            for (EmployerReportDto employerReportDto : employerReportDtos) {
                EmployerReport employerReport =
                        employerReportService.getEmployerReport(employerReportDto.getId());
                employerReports.add(employerReport);
            }
        }

        List<StudentReportDto> studentReportDtos = coopDto.getStudentReports();
        Set<StudentReport> studentReports = new HashSet<StudentReport>();

        if (employerReportDtos != null) {
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
