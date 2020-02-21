package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.ReportSectionDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
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
public class StudentReportControllerIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private CoopRepository coopRepository;
    @Autowired private CourseOfferingRepository courseOfferingRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    /**
     * Tests creating a StudentReport
     *
     * @throws Exception
     */
    @Test
    public void testStudentReportFlow() throws Exception {
        // set up entities that a Student Report needs
        File testFile = new File("src/test/resources/Test_Offer_Letter.pdf");
        MultipartFile multipartFile =
                new MockMultipartFile("Offer Letter", new FileInputStream(testFile));

        CourseDto courseDto = createTestCourse();
        CourseOfferingDto courseOfferingDto = createTestCourseOffering(courseDto);
        CoopDto coopDto =
                createTestCoop(courseOfferingDto, createTestStudent(), CoopStatus.IN_PROGRESS);

        // 1. create the StudentReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/student-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "INCOMPLETE")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(coopDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // 2. get object from response
        StudentReportDto returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
        assertEquals(returnedReport.getTitle(), "Offer Letter");

        // 3. update file

        Set<ReportSectionDto> rdtos = new HashSet<ReportSectionDto>();

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/student-reports/" + returnedReport.getId());
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
                                        .content(objectMapper.writeValueAsString(rdtos))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
        assertEquals("Offer Letter", returnedReport.getTitle());

        assertEquals(ReportStatus.COMPLETED, returnedReport.getStatus());

        // 4. delete file
        mvcResult =
                mvc.perform(
                                delete("/student-reports/" + returnedReport.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Employer Contacts
        mvcResult =
                mvc.perform(get("/student-reports").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<StudentReportDto> studentReportDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                StudentReportDto[].class));

        assertEquals(studentReportDtos.size(), 0);
    }
}
