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

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.ReportSectionRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceEmployerReportTests {
	
	@Autowired EmployerReportRepository employerReportRepository;
	@Autowired CoopRepository coopRepository;
	@Autowired CourseRepository courseRepository;
	@Autowired CourseOfferingRepository courseOfferingRepository;
	@Autowired CompanyRepository companyRepository;
	@Autowired EmployerContactRepository employerContactRepository;
	@Autowired StudentRepository studentRepository;
	@Autowired ReportSectionRepository reportSectionRepository;
	
	@Autowired EmployerReportService employerReportService;
	@Autowired CoopService coopService;
	@Autowired CourseService courseService;
	@Autowired CourseOfferingService courseOfferingService;
	@Autowired CompanyService companyService;
	@Autowired EmployerContactService employerContactService;
	@Autowired StudentService studentService;
	@Autowired ReportSectionService reportSectionService;
	
	@BeforeEach
    @AfterEach
    public void clearDatabase() {
    	coopRepository.deleteAll();
    	courseOfferingRepository.deleteAll();
    	courseRepository.deleteAll();
    	employerContactRepository.deleteAll();
    	companyRepository.deleteAll();
    	studentRepository.deleteAll();
		employerReportRepository.deleteAll();
	}
	
	@Test
	public void testCreateEmployerReport() {
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		Company company = createTestCompany();
		EmployerContact ec = createTestEmployerContact(company);
		
		try {
			employerReportService.createEmployerReport(ReportStatus.COMPLETED, coop, ec);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		assertEquals(1, employerReportService.getAllEmployerReports().size());
	}
	
	@Test
	public void testCreateEmployerReportNull() {
		String error = "";
		try {
			employerReportService.createEmployerReport(null, null, null);
		} catch  (IllegalArgumentException e){
			error = e.getMessage();
		}
		
		assertEquals("Report Status cannot be null! "
				   + "Coop cannot be null! "
				   + "Employer Contact cannot be null!", error);
	}
	
	@Test
	public void testUpdateEmployerReportWithReportSections() {
		EmployerReport er = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		Company company = createTestCompany();
		EmployerContact ec = createTestEmployerContact(company);
		
		try {
			er = employerReportService.createEmployerReport(ReportStatus.COMPLETED, coop, ec);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		List<ReportSection> sections = new ArrayList<ReportSection>();
		ReportSection rs = createTestReportSection();
		sections.add(rs);
		
		try {
			er = employerReportService.updateEmployerReport(er, ReportStatus.COMPLETED, coop, ec, sections);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(1, er.getReportSections().size());
		assertEquals(1, employerReportService.getAllEmployerReports().size());
	}
	
	@Test
	public void testUpdateEmployerReport() {
		EmployerReport er = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		Company company = createTestCompany();
		EmployerContact ec = createTestEmployerContact(company);
		
		try {
			er = employerReportService.createEmployerReport(ReportStatus.COMPLETED, coop, ec);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		List<ReportSection> sections = new ArrayList<ReportSection>();
		ReportSection rs = createTestReportSection();
		sections.add(rs);
		
		try {
			er = employerReportService.updateEmployerReport(er, ReportStatus.INCOMPLETE, coop, ec, sections);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(1, er.getReportSections().size());
		assertEquals(ReportStatus.INCOMPLETE, 
				     employerReportService.getEmployerReport(er.getId()).getStatus());
		assertEquals(1, employerReportService.getAllEmployerReports().size());
	}
	
	@Test
	public void testUpdateEmployerReportInvalid() {
		EmployerReport er = null;
		
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		Company company = createTestCompany();
		EmployerContact ec = createTestEmployerContact(company);
		
		try {
			er = employerReportService.createEmployerReport(ReportStatus.COMPLETED, coop, ec);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		String error = "";
		try {
			er = employerReportService.updateEmployerReport(null, null, null, null, null);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}
		
		assertEquals("Employer Report cannot be null! "
				   + "Report Status cannot be null! "
				   + "Coop cannot be null! "
				   + "Employer Contact cannot be null!", error);
		assertEquals(ReportStatus.COMPLETED, 
			     employerReportService.getEmployerReport(er.getId()).getStatus());
		assertEquals(1, employerReportService.getAllEmployerReports().size());
	}
	
	@Test
	public void testDeleteEmployerReport() {
		EmployerReport er = null;
		Course course = createTestCourse();
		CourseOffering courseOffering = createTestCourseOffering(course);
		Student s = createTestStudent();
		Coop coop = createTestCoop(courseOffering, s);
		Company company = createTestCompany();
		EmployerContact ec = createTestEmployerContact(company);
		
		try {
			er = employerReportService.createEmployerReport(ReportStatus.COMPLETED, coop, ec);
		} catch  (IllegalArgumentException e){
			fail();
		}
		
		assertEquals(1, employerReportService.getAllEmployerReports().size());
		
		try {
			employerReportService.deleteEmployerReport(er);
		} catch (IllegalArgumentException e) {
			fail();
		}
		
		assertEquals(0, employerReportService.getAllEmployerReports().size());
	}
	
	@Test
	public void testDeleteEmployerReportInvalid() {
		String error = "";
        try {
        	employerReportService.deleteEmployerReport(null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }

        assertEquals("Employer Report to delete cannot be null!", error);
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
    
    private Company createTestCompany() {
        Company c = new Company();
        c = companyService.createCompany("Facebook", null);
        
        return c;
        
    }
    
    private EmployerContact createTestEmployerContact(Company c) {
    	EmployerContact ec = new EmployerContact();
    	ec = employerContactService.createEmployerContact("Emma", "Eags", "eags@gmail.com", "2143546578", c);
    	return ec;
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
