package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.StudentRepository;
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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    public void testCSVParser() throws Exception {
        File testFile = new File("src/test/resources/sampleStudent.csv");
        MultipartFile multipartFile =
                new MockMultipartFile("Student CSV", new FileInputStream(testFile));
        
               
        MvcResult mvcResult =
                mvc.perform(
                                multipart("/csv-parser")
                                        .file("file", multipartFile.getBytes())
                                        .contentType(MediaType.MULTIPART_FORM_DATA)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        List<Object> students = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object[].class));
        
        assertEquals(2, students.size());
    }
}
