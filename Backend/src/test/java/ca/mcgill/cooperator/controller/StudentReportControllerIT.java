package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.model.Coop;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Course;
import ca.mcgill.cooperator.model.CourseOffering;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.model.Student;
import ca.mcgill.cooperator.service.CoopService;
import ca.mcgill.cooperator.service.CourseOfferingService;
import ca.mcgill.cooperator.service.CourseService;
import ca.mcgill.cooperator.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
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
public class StudentReportControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private CoopService coopService;
    @Autowired private StudentService studentService;
    @Autowired private CourseService courseService;
    @Autowired private CourseOfferingService courseOfferingService;

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

        Course course = createTestCourse();
        CourseOffering courseOffering = createTestCourseOffering(course);
        Coop testCoop =
                coopService.createCoop(CoopStatus.IN_PROGRESS, courseOffering, createTestStudent());

        // 1. create the StudentReport with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/student-reports")
                                        .file("file", multipartFile.getBytes())
                                        .param("status", "INCOMPLETE")
                                        .param("title", "Offer Letter")
                                        .param("coop_id", String.valueOf(testCoop.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // 2. get object from response
        StudentReportDto returnedReport =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentReportDto.class);
        assertEquals(returnedReport.getTitle(), "Offer Letter");
    }

    /* Helper methods */

    private Student createTestStudent() {
        Student s = new Student();
        s = studentService.createStudent("Susan", "Matuszewski", "susan@gmail.com", "260719281");

        return s;
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
