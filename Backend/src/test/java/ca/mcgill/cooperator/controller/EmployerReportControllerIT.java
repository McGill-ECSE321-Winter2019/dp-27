package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Report;
import ca.mcgill.cooperator.model.ReportStatus;
import ca.mcgill.cooperator.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployerReportControllerIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private ReportService reportService;

    @Autowired private ReportRepository reportRepository;
    @Autowired private CoopRepository coopRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private EmployerContactRepository employerContactRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private ReportConfigRepository reportConfigRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<Report> reports = reportService.getAllReports();
        for (Report r : reports) {
            r.setAuthor(null);
            reportRepository.save(r);
        }
        reportRepository.deleteAll();
        coopRepository.deleteAll();
        authorRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        reportConfigRepository.deleteAll();
    }

    /**
     * Tests creating an EmployerReport
     *
     * @throws Exception
     */
    @Test
    public void testEmployerReportFlow() throws Exception {
        // set up entities that an EmployerReport needs
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        CourseDto courseDto = createTestCourse();
        CourseOfferingDto courseOfferingDto = createTestCourseOffering(courseDto);
        CoopDto coopDto =
                createTestCoop(courseOfferingDto, createTestStudent(), CoopStatus.IN_PROGRESS);
        CompanyDto companyDto = createTestCompany();
        EmployerContactDto ecDto = createTestEmployerContact(companyDto);
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<CourseOfferingDto>();
        courseOfferingDtos.add(courseOfferingDto);
        ReportConfigDto reportConfigDto = createTestReportConfig(true, 14, true, "First Evaluation", courseOfferingDtos);

        // 1. create the EmployerReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "INCOMPLETE")
                                        .param("title", "Offer Letter")
                                        .param("coopId", String.valueOf(coopDto.getId()))
                                        .param("authorId", String.valueOf(ecDto.getId()))
                                        .param("reportConfigId", String.valueOf(reportConfigDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // 2. get object from response
        ReportDto returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);
        assertEquals(returnedReport.getTitle(), "Offer Letter");

        // 3. update file
        Set<ReportSectionDto> rsDtos = new HashSet<ReportSectionDto>();

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/reports/" + returnedReport.getId());
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
                                        .param("authorId", String.valueOf(ecDto.getId()))
                                        .param("reportConfigId", String.valueOf(reportConfigDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsDtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportDto.class);
        assertEquals("Offer Letter", returnedReport.getTitle());

        assertEquals(ReportStatus.COMPLETED, returnedReport.getStatus());

        // 4. delete file
        mvcResult =
                mvc.perform(
                                delete("/reports/" + returnedReport.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        mvcResult =
                mvc.perform(
                                get("/reports/author/" + ecDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<ReportDto> reportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), ReportDto[].class));

        assertEquals(reportDtos.size(), 0);
    }
}
