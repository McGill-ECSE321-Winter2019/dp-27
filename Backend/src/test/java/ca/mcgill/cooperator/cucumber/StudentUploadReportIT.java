package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.ReportSectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
public class StudentUploadReportIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentRepository studentReportRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;

    @Autowired private CoopDetailsService coopDetailsService;
    @Autowired private ReportSectionService reportSectionService;

    MvcResult mvcResult;

    StudentDto studentDto;
    CourseDto courseDto;
    CourseOfferingDto courseOfferingDto;
    CoopDto coopDto;
    CoopDetailsDto coopDetailsDto;
    CompanyDto companyDto;
    EmployerContactDto employerContactDto;
    StudentReportDto studentReportDto;
    ReportSectionDto reportSectionDto;

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

        reportSections = reportSectionService.getAllReportSections();
        assertEquals(0, reportSections.size());

        studentReportRepository.deleteAll();

        // deleting all students will also delete all coops
        studentRepository.deleteAll();
        // deleting all companies will also delete all employer contacts
        companyRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopDetailsRepository.deleteAll();
    }

    @Given("the Student is currently doing a Coop term")
    public void studentHasCurrentCoopTerm() throws Exception {
        studentDto = createTestStudent();

        courseDto = createTestCourse("FACC200");
        courseOfferingDto = createTestCourseOffering(courseDto, 2020, Season.WINTER);
        coopDto = createTestCoop(courseOfferingDto, studentDto, CoopStatus.IN_PROGRESS);
        companyDto = createTestCompany("Lightspeed", "Montreal", "Quebec", "Canada");
        employerContactDto =
                createTestEmployerContact(
                        companyDto, "John", "Smith", "smithy@smithy.com", "12345678");
        coopDetailsDto = createTestCoopDetails(coopDto, 2500, 40, employerContactDto);
    }

    @And("the Student has a Report due")
    public void studentHasReportDue() throws Exception {
        mvcResult =
                mvc.perform(
                                multipart("/student-reports")
                                        .file("file", null)
                                        .param("status", "NOT_SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);

        assertEquals(ReportStatus.NOT_SUBMITTED, studentReportDto.getStatus());
        assertEquals("Offer Letter", studentReportDto.getTitle());
    }

    @When("the Student uploads the proper Report")
    public void studentUploadsReport() throws Exception {
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        Set<ReportSectionDto> rdtos = new HashSet<ReportSectionDto>();

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/student-reports/" + studentReportDto.getId());
        builder.with(
                new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(
                            MockHttpServletRequest request) {
                        request.setMethod("PUT");
                        return request;
                    }
                });
        mvcResult =
                mvc.perform(
                                builder.file("file", multipartFile.getBytes())
                                        .param("status", "SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rdtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
    }

    @Then("the Report is saved in the system")
    public void saveReport() {
        assertEquals(ReportStatus.SUBMITTED, studentReportDto.getStatus());
        assertEquals("Offer Letter", studentReportDto.getTitle());
    }

    @And("the Student has uploaded a Report type previously")
    public void studentHasUploadedReportPreviously() throws Exception {
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        mvcResult =
                mvc.perform(
                                multipart("/student-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);

        assertEquals(ReportStatus.SUBMITTED, studentReportDto.getStatus());
        assertEquals("Offer Letter", studentReportDto.getTitle());
    }

    @When("the Student uploads the same type of Report again")
    public void studentUploadsReportAgain() throws Exception {
        Set<ReportSectionDto> rdtos = new HashSet<ReportSectionDto>();
        reportSectionDto = createTestReportSection(studentReportDto);
        rdtos.add(reportSectionDto);

        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/student-reports/" + studentReportDto.getId());
        builder.with(
                new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(
                            MockHttpServletRequest request) {
                        request.setMethod("PUT");
                        return request;
                    }
                });
        mvcResult =
                mvc.perform(
                                builder.file("file", multipartFile.getBytes())
                                        .param("status", "SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rdtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
    }

    @Then("the new Report overwrites the old one in the system")
    public void overwriteReport() {
        assertEquals(1, studentReportDto.getReportSections().size());
        assertEquals("Offer Letter", studentReportDto.getTitle());
    }
}
