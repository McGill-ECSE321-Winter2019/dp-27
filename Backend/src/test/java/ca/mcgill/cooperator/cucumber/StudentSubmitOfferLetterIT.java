package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.controller.ControllerUtils;
import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentSubmitOfferLetterIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private StudentRepository studentRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;

    @Autowired private StudentService studentService;
    @Autowired private EmployerContactService employerContactService;
    @Autowired private CompanyService companyService;
    @Autowired private CourseService courseService;
    @Autowired private CourseOfferingService courseOfferingService;
    @Autowired private CoopDetailsService coopDetailsService;

    /* Global test variables */

    Student student;
    Course course;
    CourseOffering courseOffering;
    EmployerContact employerContact;
    CoopDto testCoop;

    @Before
    public void clearDatabase() {
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }
        // deleting all students will also delete all coops
        studentRepository.deleteAll();
        // deleting all companies will also delete all employer contacts
        companyRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopDetailsRepository.deleteAll();
    }

    @When("the Student uploads a copy of their offer letter")
    public void studentSubmitsOfferLetter() throws Exception {
        // set up global variables
        student = createTestStudent();
        employerContact = createTestEmployerContact();
        course = createTestCourse();
        courseOffering = createTestCourseOffering(course);
        testCoop = createTestCoop(CoopStatus.UNDER_REVIEW, courseOffering, student);

        // set up file to upload
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        // upload the StudentReport with a POST request
        mvc.perform(
                        multipart("/student-reports")
                                .file("file", multipartFile.getBytes())
                                .param("status", "IN_REVIEW")
                                .param("title", "Offer Letter")
                                .param("coop_id", String.valueOf(testCoop.getId()))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @And("submits the details of their Coop term")
    public void studentSubmitsCoopDetails() throws Exception {
        CoopDetailsDto coopDetails = new CoopDetailsDto();
        coopDetails.setCoop(testCoop);
        coopDetails.setEmployerContact(ControllerUtils.convertToDto(employerContact));
        coopDetails.setHoursPerWeek(40);
        // pay per hour is in cents, this is $30 per hour
        coopDetails.setPayPerHour(3000);

        // create the coop details with a POST request
        mvc.perform(
                        post("/coop-details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(coopDetails))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

    @Then("the offer letter is put up for review by an Admin")
    public void offerLetterIsPutUpForReview() throws Exception {
        MvcResult mvcResult =
                mvc.perform(
                                get("/student-reports")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<StudentReportDto> returnedReports =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                StudentReportDto[].class));

        assertEquals(1, returnedReports.size());

        StudentReportDto report = returnedReports.get(0);

        assertEquals(ReportStatus.IN_REVIEW, report.getStatus());
        assertEquals(CoopStatus.UNDER_REVIEW, report.getCoop().getStatus());
        assertEquals("susan@gmail.com", report.getCoop().getStudent().getEmail());
    }

    private Student createTestStudent() {
        Student s =
                studentService.createStudent(
                        "Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
    }

    private EmployerContact createTestEmployerContact() {
        Company c =
                companyService.createCompany(
                        "Facebook",
                        "Menlo Park",
                        "California",
                        "USA",
                        new ArrayList<EmployerContact>());
        return employerContactService.createEmployerContact(
                "Albert", "Kragl", "albertkragl@fb.com", "12345678", c);
    }

    private CoopDto createTestCoop(
            CoopStatus status, CourseOffering courseOffering, Student student) throws Exception {
        CoopDto coopDto = new CoopDto();
        coopDto.setStatus(status);
        coopDto.setCourseOffering(ControllerUtils.convertToDto(courseOffering));
        coopDto.setStudent(ControllerUtils.convertToDto(student));

        // 1. create the Co-op with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        CoopDto returnedCoop =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CoopDto.class);

        return returnedCoop;
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
}
