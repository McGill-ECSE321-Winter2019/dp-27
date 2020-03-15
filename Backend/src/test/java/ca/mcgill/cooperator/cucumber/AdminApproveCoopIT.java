package ca.mcgill.cooperator.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.dto.StudentReportSectionDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.model.StudentReportSection;
import ca.mcgill.cooperator.service.CoopDetailsService;
import ca.mcgill.cooperator.service.StudentReportSectionService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AdminApproveCoopIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private StudentRepository studentRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private CoopDetailsRepository coopDetailsRepository;

    @Autowired private CoopDetailsService coopDetailsService;
    @Autowired private StudentReportSectionService studentReportSectionService;

    /* Global test variables */
    StudentDto studentDto;
    CourseDto courseDto;
    CourseOfferingDto courseOfferingDto;
    CoopDto coopDto;
    StudentReportDto studentReportDto;
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

        List<StudentReportSection> reportSections =
                studentReportSectionService.getAllReportSections();
        for (StudentReportSection reportSection : reportSections) {
            studentReportSectionService.deleteReportSection(reportSection);
        }

        // deleting all students will also delete all coops
        studentRepository.deleteAll();
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
                                multipart("/student-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "UNDER_REVIEW")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
        assertEquals(studentReportDto.getTitle(), "Offer Letter");
    }

    @Then("the Admin approves the Coop term")
    public void adminApprovesCoopTerm() throws Exception {
        MvcResult mvcResult =
                mvc.perform(
                                get("/student-reports")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<StudentReportDto> studentReportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                StudentReportDto[].class));

        assertEquals(1, studentReportDtos.size());

        studentReportDto = studentReportDtos.get(0);

        assertEquals(ReportStatus.UNDER_REVIEW, studentReportDto.getStatus());
        assertEquals(CoopStatus.UNDER_REVIEW, studentReportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", studentReportDto.getCoop().getStudent().getEmail());

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
                                put("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // update student report status
        studentReportDto.setStatus(ReportStatus.COMPLETED);

        Set<StudentReportSectionDto> rsDtos = new HashSet<StudentReportSectionDto>();

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
                                        .param("status", "COMPLETED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get student report dto
        mvcResult =
                mvc.perform(
                                get("/student-reports/" + studentReportDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);

        assertEquals(ReportStatus.COMPLETED, studentReportDto.getStatus());
        assertEquals(CoopStatus.FUTURE, studentReportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", studentReportDto.getCoop().getStudent().getEmail());
    }

    @Then("the Admin rejects the Coop term")
    public void adminRejectsCoopTerm() throws Exception {
        MvcResult mvcResult =
                mvc.perform(
                                get("/student-reports")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<StudentReportDto> studentReportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                StudentReportDto[].class));

        assertEquals(1, studentReportDtos.size());

        studentReportDto = studentReportDtos.get(0);

        assertEquals(ReportStatus.UNDER_REVIEW, studentReportDto.getStatus());
        assertEquals(CoopStatus.UNDER_REVIEW, studentReportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", studentReportDto.getCoop().getStudent().getEmail());

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
        coopDto.setStatus(CoopStatus.INCOMPLETE);
        companyDto = createTestCompany();
        employerContactDto = createTestEmployerContact(companyDto);
        coopDetailsDto = createTestCoopDetails(coopDto, employerContactDto);
        coopDto.setCoopDetails(coopDetailsDto);

        mvcResult =
                mvc.perform(
                                put("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // update student report status
        Set<StudentReportSectionDto> rsDtos = new HashSet<StudentReportSectionDto>();

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
                                        .param("status", "COMPLETED")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get student report dto
        mvcResult =
                mvc.perform(
                                get("/student-reports/" + studentReportDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        studentReportDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);

        assertEquals(ReportStatus.COMPLETED, studentReportDto.getStatus());
        assertEquals(CoopStatus.INCOMPLETE, studentReportDto.getCoop().getStatus());
        assertEquals("susan@gmail.com", studentReportDto.getCoop().getStudent().getEmail());
    }
}
