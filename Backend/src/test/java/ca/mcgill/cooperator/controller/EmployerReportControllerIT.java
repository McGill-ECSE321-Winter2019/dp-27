package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.model.Company;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CompanyService;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
public class EmployerReportControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private CoopService coopService;
    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private CourseOfferingService courseOfferingService;
    @Autowired private CompanyService companyService;
    @Autowired private EmployerContactService employerContactService;

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

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Coop testCoop =
                coopService.createCoop(CoopStatus.IN_PROGRESS, courseOffering, createTestStudent());

        EmployerContact ec = createTestEmployerContact();

        // 1. create the EmployerReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/employer-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "INCOMPLETE")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(testCoop.getId()))
                                        .param("employer_id", String.valueOf(ec.getId()))
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

    /* Helper methods */

    private Student createTestStudent() {
        return studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");
    }

    private EmployerContact createTestEmployerContact() {
        Company c =
                companyService.createCompany(
                        "Facebook",
                        "USA",
                        "Menlo Park",
                        "California",
                        new ArrayList<EmployerContact>());
        return employerContactService.createEmployerContact(
                "Albert", "Kragl", "albertkragl@fb.com", "12345678", c);
    }

    private Course createTestCourse() {
        return courseService.createCourse("FACC200");
    }

    private CourseOffering createTestCourseOffering(Course c) {
        return courseOfferingService.createCourseOffering(2020, Season.WINTER, c);
    }
}
