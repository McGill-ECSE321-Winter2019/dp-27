package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.service.EmployerReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class EmployerReportControllerIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private EmployerReportService employerReportService;

    @Autowired private CoopRepository coopRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private EmployerContactRepository employerContactRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private EmployerReportRepository employerReportRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<EmployerReport> ecs = employerReportService.getAllEmployerReports();
        for (EmployerReport ec : ecs) {
            ec.setEmployerContact(null);
            employerReportRepository.save(ec);
        }
        employerReportRepository.deleteAll();
        coopRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
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
        CoopDto coopDto = createTestCoop(courseOfferingDto, createTestStudent());
        CompanyDto companyDto = createTestCompany();
        EmployerContactDto ecDto = createTestEmployerContact(companyDto);

        // 1. create the EmployerReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/employer-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "INCOMPLETE")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .param("employer_id", String.valueOf(ecDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // 2. get object from response
        EmployerReportDto returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerReportDto.class);
        assertEquals(returnedReport.getTitle(), "Offer Letter");
    }
}
