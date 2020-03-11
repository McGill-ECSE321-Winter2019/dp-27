package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Season;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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
public class CSVParserControllerIT extends ControllerIT {
    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired StudentRepository studentRepository;
    @Autowired CoopRepository coopRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        coopRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void testCSVParserCorrect() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setEmail("albert.kragl@mail.mcgill.ca");
        studentDto.setFirstName("Albert");
        studentDto.setLastName("Kragl");
        studentDto.setStudentId("260732298");
        MvcResult mvcResult = mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(studentDto)).characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        studentDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentDto.class);
        
        StudentDto studentDto2 = new StudentDto();
        studentDto.setEmail("emma.eagles@mail.mcgill.ca");
        studentDto.setFirstName("Emma");
        studentDto.setLastName("Eagles");
        studentDto.setStudentId("260709533");
        
        mvcResult = mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(studentDto)).characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        
        studentDto2 =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentDto.class);
        
        //Make coop, make course offering, make course
        CourseDto courseDto = new CourseDto();
        courseDto.setName("ECSE201");
        
        mvcResult = mvc.perform(
                post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        
        CourseOfferingDto courseOfferingDto = new CourseOfferingDto();
        courseOfferingDto.setYear(2020);
        courseOfferingDto.setSeason(Season.SUMMER);
        courseOfferingDto.setCourse(courseDto);
        
        mvcResult = mvc.perform(
                post("/course-offerings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseOfferingDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        courseOfferingDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseOfferingDto.class);
        
        CoopDto coopDto = new CoopDto();
        coopDto.setStudent(studentDto);
        coopDto.setStatus(CoopStatus.FUTURE);
        coopDto.setCourseOffering(courseOfferingDto);
        
        mvc.perform(
                post("/coops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coopDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        coopDto.setStudent(studentDto2);
        coopDto.setStatus(CoopStatus.FUTURE);
        coopDto.setCourseOffering(courseOfferingDto);
        
        mvc.perform(
                post("/coops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coopDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        File testFile = new File("src/test/resources/sampleStudent.csv");
        MultipartFile multipartFile =
                new MockMultipartFile("Student CSV", new FileInputStream(testFile));
        
               
        mvcResult =
                mvc.perform(
                                multipart("/csv-parser")
                                        .file("file", multipartFile.getBytes())
                                        .param("course_id", String.valueOf(courseOfferingDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        
        List<Object> students = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object[].class));
        
        assertEquals(0, students.size());
    }
    
    @Test
    public void testCSVParserWithUnregisteredStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setEmail("albert.kragl@mail.mcgill.ca");
        studentDto.setFirstName("Albert");
        studentDto.setLastName("Kragl");
        studentDto.setStudentId("260732298");
        MvcResult mvcResult = mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(studentDto)).characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        
        studentDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentDto.class);
        
        //Make coop, make course offering, make course
        CourseDto courseDto = new CourseDto();
        courseDto.setName("ECSE201");
        
        mvcResult = mvc.perform(
                post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        
        CourseOfferingDto courseOfferingDto = new CourseOfferingDto();
        courseOfferingDto.setYear(2020);
        courseOfferingDto.setSeason(Season.SUMMER);
        courseOfferingDto.setCourse(courseDto);
        
        mvcResult = mvc.perform(
                post("/course-offerings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseOfferingDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        courseOfferingDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseOfferingDto.class);
        
        CoopDto coopDto = new CoopDto();
        coopDto.setStudent(studentDto);
        coopDto.setStatus(CoopStatus.FUTURE);
        coopDto.setCourseOffering(courseOfferingDto);
        
        mvc.perform(
                post("/coops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coopDto))
                        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();
        
        File testFile = new File("src/test/resources/sampleStudent.csv");
        MultipartFile multipartFile =
                new MockMultipartFile("Student CSV", new FileInputStream(testFile));
        
               
        mvcResult =
                mvc.perform(
                                multipart("/csv-parser")
                                        .file("file", multipartFile.getBytes())
                                        .param("course_id", String.valueOf(courseOfferingDto.getId()))
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        
        List<Object> students = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object[].class));
        
        assertEquals(1, students.size());
    }
}
