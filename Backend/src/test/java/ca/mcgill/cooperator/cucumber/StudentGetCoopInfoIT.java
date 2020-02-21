package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.controller.ControllerIT;
import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.ReportSectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
public class StudentGetCoopInfoIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    MvcResult mvcResult;

    StudentDto studentDto;
    CourseDto courseDto1;
    CourseOfferingDto courseOfferingDto1;
    CoopDto coopDto1;
    CoopDetailsDto coopDetailsDto1;
    CompanyDto companyDto1;
    EmployerContactDto employerContactDto1;

    CourseDto courseDto2;
    CourseOfferingDto courseOfferingDto2;
    CoopDto coopDto2;
    CoopDetailsDto coopDetailsDto2;
    CompanyDto companyDto2;
    EmployerContactDto employerContactDto2;

    @Autowired private StudentRepository studentRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;

    @Autowired private CoopDetailsService coopDetailsService;
    @Autowired private ReportSectionService reportSectionService;

    @Before
    @After
    public void clearDatabase() {
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }

        List<ReportSection> reportSections = reportSectionService.getAllReportSections();
        for (ReportSection reportSection : reportSections) {
            reportSectionService.deleteReportSection(reportSection);
        }

        // deleting all students will also delete all coops
        studentRepository.deleteAll();
        // deleting all companies will also delete all employer contacts
        companyRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopDetailsRepository.deleteAll();
    }

    @Given("the Student has at least one current or previous Coop term")
    public void studentHasAtLeastOneCoopTerm() throws Exception {
        studentDto = createTestStudent();

        // coop 1
        courseDto1 = createTestCourse("FACC200");
        courseOfferingDto1 = createTestCourseOffering(courseDto1, 2018, Season.FALL);
        coopDto1 = createTestCoop(courseOfferingDto1, studentDto, CoopStatus.COMPLETED);
        companyDto1 = createTestCompany("Lightspeed", "Montreal", "Quebec", "Canada");
        employerContactDto1 =
                createTestEmployerContact(
                        companyDto1, "John", "Smith", "smithy@smithy.com", "12345678");
        coopDetailsDto1 = createTestCoopDetails(coopDto1, 2500, 40, employerContactDto1);

        // coop 2
        courseDto2 = createTestCourse("FACC201");
        courseOfferingDto2 = createTestCourseOffering(courseDto2, 2019, Season.SUMMER);
        coopDto2 = createTestCoop(courseOfferingDto2, studentDto, CoopStatus.COMPLETED);
        companyDto2 = createTestCompany("Facebook", "Montreal", "Quebec", "Canada");
        employerContactDto2 =
                createTestEmployerContact(companyDto2, "Mark", "Zucc", "zucc@fb.com", "111453678");
        coopDetailsDto2 = createTestCoopDetails(coopDto2, 3000, 40, employerContactDto2);
    }

    @When("the Student requests to get their Coop term information")
    public void studentRequestsCoopInformation() throws Exception {
        mvcResult =
                mvc.perform(
                                get("/coops/student/" + studentDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Then("the system returns the information for all of their Coop terms")
    public void getCoopInformationForStudent() throws Exception {
        // get object from response
        List<CoopDto> coopDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CoopDto[].class));

        assertEquals(2, coopDtos.size());

        coopDto1 = coopDtos.get(0);
        coopDto2 = coopDtos.get(1);

        assertEquals("FACC200", coopDto1.getCourseOffering().getCourse().getName());
        assertEquals("FACC201", coopDto2.getCourseOffering().getCourse().getName());

        assertEquals("John", coopDto1.getCoopDetails().getEmployerContact().getFirstName());
        assertEquals("Mark", coopDto2.getCoopDetails().getEmployerContact().getFirstName());

        assertEquals(
                "Lightspeed",
                coopDto1.getCoopDetails().getEmployerContact().getCompany().getName());
        assertEquals(
                "Facebook", coopDto2.getCoopDetails().getEmployerContact().getCompany().getName());

        assertEquals(2500, coopDto1.getCoopDetails().getPayPerHour());
        assertEquals(3000, coopDto2.getCoopDetails().getPayPerHour());

        assertEquals(Season.FALL, coopDto1.getCourseOffering().getSeason());
        assertEquals(Season.SUMMER, coopDto2.getCourseOffering().getSeason());
    }
}
