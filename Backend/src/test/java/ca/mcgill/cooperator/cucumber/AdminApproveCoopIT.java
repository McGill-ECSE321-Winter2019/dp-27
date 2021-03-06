package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.controller.BaseControllerIT;
import ca.mcgill.cooperator.dao.AuthorRepository;
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
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportSection;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.ReportSectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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

public class AdminApproveCoopIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private StudentRepository studentRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;
    @Autowired AuthorRepository authorRepository;

    @Autowired private CoopDetailsService coopDetailsService;
    @Autowired private ReportSectionService reportSectionService;

    /* Global test variables */
    StudentDto studentDto;
    CourseDto courseDto;
    CourseOfferingDto courseOfferingDto;
    CoopDto coopDto;
    ReportDto reportDto;
    CoopDetailsDto coopDetailsDto;
    EmployerContactDto employerContactDto;
    CompanyDto companyDto;
    MultipartFile multipartFile;

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
        authorRepository.deleteAll();
        // deleting all companies will also delete all employer contacts
        companyRepository.deleteAll();
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        coopDetailsRepository.deleteAll();
    }

    @Given("a Student has a coop they would like to register")
    public void createStudentandCoop() throws Exception {
        studentDto = createTestStudent();
        courseDto = createTestCourse();
        courseOfferingDto = createTestCourseOffering(courseDto);
        coopDto = createTestCoop(courseOfferingDto, studentDto, CoopStatus.UNDER_REVIEW);
    }

    @When("the Student submits their offer letter")
    public void studentSubmitsOfferLetter() throws Exception {

        // set up file to upload
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        multipartFile = new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        // upload the StudentReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "UNDER_REVIEW")
                                        .param("title", "Offer Letter")
                                        .param("coopId", String.valueOf(coopDto.getId()))
                                        .param("authorId", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);
        assertEquals(reportDto.getTitle(), "Offer Letter");
    }

    @Then("the Admin approves the Coop term")
    public void adminApprovesCoopTerm() throws Exception {
        MvcResult mvcResult =
                mvc.perform(
                                get("/reports")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<ReportDto> studentReportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), ReportDto[].class));

        assertEquals(1, studentReportDtos.size());

        reportDto = studentReportDtos.get(0);

        assertEquals(ReportStatus.UNDER_REVIEW, reportDto.getStatus());
        assertEquals(CoopStatus.UNDER_REVIEW, reportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", reportDto.getCoop().getStudent().getEmail());

        mvcResult =
                mvc.perform(
                                get("/coops/" + coopDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        coopDto =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CoopDto.class);

        // update coop status
        coopDto.setStatus(CoopStatus.FUTURE);
        companyDto = createTestCompany();
        employerContactDto = createTestEmployerContact(companyDto);
        coopDetailsDto = createTestCoopDetails(coopDto, employerContactDto);
        coopDto.setCoopDetails(coopDetailsDto);

        mvcResult =
                mvc.perform(
                                put("/coops/" + coopDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // update student report status
        reportDto.setStatus(ReportStatus.COMPLETED);

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
                                        .param("status", "COMPLETED")
                                        .param("title", "Offer Letter")
                                        .param("coopId", String.valueOf(coopDto.getId()))
                                        .param("authorId", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get student report dto
        mvcResult =
                mvc.perform(
                                get("/reports/" + reportDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);

        assertEquals(ReportStatus.COMPLETED, reportDto.getStatus());
        assertEquals(CoopStatus.FUTURE, reportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", reportDto.getCoop().getStudent().getEmail());
    }

    @Then("the Admin rejects the Coop term")
    public void adminRejectsCoopTerm() throws Exception {
        MvcResult mvcResult =
                mvc.perform(
                                get("/reports")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<ReportDto> studentReportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), ReportDto[].class));

        assertEquals(1, studentReportDtos.size());

        reportDto = studentReportDtos.get(0);

        assertEquals(ReportStatus.UNDER_REVIEW, reportDto.getStatus());
        assertEquals(CoopStatus.UNDER_REVIEW, reportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", reportDto.getCoop().getStudent().getEmail());

        mvcResult =
                mvc.perform(
                                get("/coops/" + coopDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        coopDto =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CoopDto.class);

        // update coop status
        coopDto.setStatus(CoopStatus.REJECTED);
        companyDto = createTestCompany();
        employerContactDto = createTestEmployerContact(companyDto);
        coopDetailsDto = createTestCoopDetails(coopDto, employerContactDto);
        coopDto.setCoopDetails(coopDetailsDto);

        mvcResult =
                mvc.perform(
                                put("/coops/" + coopDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // update student report status
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
                                        .param("status", "COMPLETED")
                                        .param("title", "Offer Letter")
                                        .param("coopId", String.valueOf(coopDto.getId()))
                                        .param("authorId", String.valueOf(studentDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get student report dto
        mvcResult =
                mvc.perform(
                                get("/reports/" + reportDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        reportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);

        assertEquals(studentDto.getFirstName(), reportDto.getAuthor().getFirstName());
        assertEquals(ReportStatus.COMPLETED, reportDto.getStatus());
        assertEquals(CoopStatus.REJECTED, reportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", reportDto.getCoop().getStudent().getEmail());
    }
}
