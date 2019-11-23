package ca.mcgill.cooperator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.model.StudentReport;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceStudentReportTests {
	@Autowired StudentReportRepository studentReportRepository;
	@Autowired CoopRepository coopRepository;
	@Autowired CourseRepository courseRepository;
	@Autowired CourseOfferingRepository courseOfferingRepository;
	@Autowired StudentRepository studentRepository;
	@Autowired ReportSectionRepository reportSectionRepository;
	
	@Autowired StudentReportService studentReportService;
	@Autowired CoopService coopService;
	@Autowired CourseService courseService;
	@Autowired CourseOfferingService courseOfferingService;
	@Autowired StudentService studentService;
	@Autowired ReportSectionService reportSectionService;
	
	@BeforeEach
    @AfterEach
    public void clearDatabase() {
		coopRepository.deleteAll();
    	courseOfferingRepository.deleteAll();
    	courseRepository.deleteAll();
    	studentRepository.deleteAll();
    	reportSectionRepository.deleteAll();
    	studentReportRepository.deleteAll();
	}
	
	@Test
	public void testCreateStudentReport() {
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		
		try {
			studentReportService.createStudentReport(ReportStatus.COMPLETED, coop);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		assertEquals(1, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testCreateStudentReportNull() {
		String error = "";
		try {
			studentReportService.createStudentReport(null, null);
		} catch  (IllegalArgumentException e){
			error = e.getMessage();
		}
		
		assertEquals("Report Status cannot be null! "
				   + "Coop cannot be null!", error);
		assertEquals(0, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testUpdateStudentReportWithReportSections() {
		StudentReport sr = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		
		try {
			sr = studentReportService.createStudentReport(ReportStatus.COMPLETED, coop);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		List<ReportSection> sections = new ArrayList<ReportSection>();
		ReportSection rs = createTestReportSection();
		sections.add(rs);
		
		try {
			sr = studentReportService.updateStudentReport(sr, ReportStatus.COMPLETED, coop, sections);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(1, sr.getReportSections().size());
		assertEquals(1, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testUpdateStudentrReport() {
		StudentReport sr = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		
		try {
			sr = studentReportService.createStudentReport(ReportStatus.COMPLETED, coop);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		List<ReportSection> sections = new ArrayList<ReportSection>();
		ReportSection rs = createTestReportSection();
		sections.add(rs);
		
		try {
			sr = studentReportService.updateStudentReport(sr, ReportStatus.INCOMPLETE, coop, sections);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(1, sr.getReportSections().size());
		assertEquals(ReportStatus.INCOMPLETE, 
				     studentReportService.getStudentReport(sr.getId()).getStatus());
		assertEquals(1, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testUpdateStudentReportInvalid() {
		StudentReport sr = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		
		try {
			sr = studentReportService.createStudentReport(ReportStatus.COMPLETED, coop);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		String error = "";
		try {
			sr = studentReportService.updateStudentReport(null, null, null, null);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertEquals("Student Report cannot be null! "
				   + "Report Status cannot be null! "
				   + "Coop cannot be null!", error);
		assertEquals(ReportStatus.COMPLETED, 
				studentReportService.getStudentReport(sr.getId()).getStatus());
		assertEquals(1, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testDeleteStudentReport() {
		StudentReport sr = null;
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		
		try {
			sr = studentReportService.createStudentReport(ReportStatus.COMPLETED, coop);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		assertEquals(1, studentReportService.getAllStudentReports().size());
		
		try {
			studentReportService.deleteStudentReport(sr);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(0, studentReportService.getAllStudentReports().size());
	}
	
	@Test
	public void testDeleteStudentReportInvalid() {
		String error = "";
        try {
        	studentReportService.deleteStudentReport(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Student Report to delete cannot be null!", error);
	}
	
	private Course createTestCourse() {
    	Course c = null;
    	c = courseService.createCourse("FACC200");
    	return c;
    }
    
    private CourseOffering createTestCourseOffering(Course c) {
    	CourseOffering co = null;
    	co = courseOfferingService.createCourseOffering(2020, Season.WINTER, c);
    	return co;
    }
    
    private Coop createTestCoop(CourseOffering co, Student s) {
    	Coop coop = new Coop();
    	coop = coopService.createCoop(CoopStatus.FUTURE, co, s);
    	return coop;
    }
    
    private Student createTestStudent() {
    	Student s = new Student();
    	s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");
    	
    	return s;
    }
    
    private ReportSection createTestReportSection() {
    	ReportSection rs = new ReportSection();
    	rs = reportSectionService.createReportSection("Hello", "This is a report section");
    	return rs;
    }
}
