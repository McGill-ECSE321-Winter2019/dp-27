package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.controller.BaseControllerIT;
import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportResponseType;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

public class StudentUploadReportIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentRepository studentReportRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;
    @Autowired private ReportConfigRepository reportConfigRepository;
    @Autowired AuthorRepository authorRepository;

    @Autowired private CoopDetailsService coopDetailsService;
    @Autowired private ReportSectionService reportSectionService;

    MvcResult mvcResult;

    /* Global test variables */

    StudentDto studentDto;
    CourseDto courseDto;
    CourseOfferingDto courseOfferingDto;
    CoopDto coopDto;
    CoopDetailsDto coopDetailsDto;
    CompanyDto companyDto;
    EmployerContactDto employerContactDto;
    ReportDto reportDto;
    ReportSectionDto reportSectionDto;
    ReportConfigDto reportConfigDto;
    ReportSectionConfigDto reportSectionConfigDto;

    @Before
    @After
    public void clearDatabase() {

        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }

        List<ReportSection> reportSections =
                reportSectionService.getAllReportSections();
        for (ReportSection reportSection : reportSections) {
            reportSectionService.deleteReportSection(reportSection);
        }

        reportSections = reportSectionService.getAllReportSections();
        assertEquals(0, reportSections.size());

        studentReportRepository.deleteAll();

        // deleting all students will also delete all coops
        studentRepository.deleteAll();
        authorRepository.deleteAll();
        // deleting all companies will also delete all employer contacts
        companyRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopDetailsRepository.deleteAll();
        reportConfigRepository.deleteAll();
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
        reportConfigDto = createTestReportConfig(true, 14, true, "Evaluation");
        reportSectionConfigDto =
                createTestReportSectionConfig(
                        "How was your co-op?", ReportResponseType.LONG_TEXT, reportConfigDto);
    }

    @And("the Student has a Report due")
    public void studentHasReportDue() throws Exception {
        mvcResult =
                mvc.perform(
                                multipart("/reports")
                                        .file("file", null)
                                        .param("status", "NOT_SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .param("author_id", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);

        assertEquals(ReportStatus.NOT_SUBMITTED, reportDto.getStatus());
        assertEquals("Offer Letter", reportDto.getTitle());
    }

    @When("the Student uploads the proper Report")
    public void studentUploadsReport() throws Exception {
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        Set<ReportSectionDto> rsDtos = new HashSet<ReportSectionDto>();

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/reports/" + reportDto.getId());
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
                                        .param("author_id", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);
    }

    @Then("the Report is saved in the system")
    public void saveReport() {
        assertEquals(ReportStatus.SUBMITTED, reportDto.getStatus());
        assertEquals("Offer Letter", reportDto.getTitle());
    }

    @And("the Student has uploaded a Report type previously")
    public void studentHasUploadedReportPreviously() throws Exception {
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        mvcResult =
                mvc.perform(
                                multipart("/reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "SUBMITTED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .param("author_id", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);

        assertEquals(ReportStatus.SUBMITTED, reportDto.getStatus());
        assertEquals("Offer Letter", reportDto.getTitle());
    }

    @When("the Student uploads the same type of Report again")
    public void studentUploadsReportAgain() throws Exception {
        Set<ReportSectionDto> rdtos = new HashSet<ReportSectionDto>();
        reportSectionDto =
                createTestReportSection(reportSectionConfigDto, reportDto);
        rdtos.add(reportSectionDto);

        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/reports/" + reportDto.getId());
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
                                        .param("author_id",  String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rdtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);
    }

    @Then("the new Report overwrites the old one in the system")
    public void overwriteReport() {
        assertEquals(1, reportDto.getReportSections().size());
        assertEquals("Offer Letter", reportDto.getTitle());
    }
}
