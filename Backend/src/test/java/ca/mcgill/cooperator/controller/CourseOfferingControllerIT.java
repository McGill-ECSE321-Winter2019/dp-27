package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.service.CourseOfferingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourseOfferingControllerIT extends ControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired CourseOfferingService courseOfferingService;

    @BeforeEach 
    @AfterEach
    public void clearDatabase() {
    	courseOfferingRepository.deleteAll();
        courseRepository.deleteAll();
    }

    /**
     * Tests creating, reading, updating and deleting a course
     *
     * @throws Exception
     */
    @Test
    public void testCourseOfferingFlow() throws Exception {
        // 1. create the Course with the helper method
        CourseDto courseDto = createTestCourse();
        
        // 2. create the CourseOffering with a post request
        CourseOfferingDto courseOfferingDto = new CourseOfferingDto();
        courseOfferingDto.setYear(2020);
        courseOfferingDto.setSeason(Season.WINTER);
        courseOfferingDto.setCourse(courseDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/course-offerings")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(courseOfferingDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        courseOfferingDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseOfferingDto.class);


        // 2. get the courseOffering by ID, valid
        mvc.perform(
                        get("/course-offerings/" + courseOfferingDto.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all CourseOfferings
        mvcResult =
                mvc.perform(get("/course-offerings").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<CourseOfferingDto> returnedCourseOfferings =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CourseOfferingDto[].class));

        assertEquals(returnedCourseOfferings.size(), 1);
        
        // 4. test getting all CourseOfferings by course
        mvcResult =
                mvc.perform(get("/course-offerings/by-course/" + courseDto.getId()).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedCourseOfferings =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CourseOfferingDto[].class));

        assertEquals(returnedCourseOfferings.size(), 1);

        CourseOfferingDto coToUpdate = returnedCourseOfferings.get(0);

        
        // 4. update the Course with a PUT request
        coToUpdate.setYear(2021);
   
        mvcResult =
                mvc.perform(
                                put("/course-offerings")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get the course offering by ID
        mvcResult =
                mvc.perform(
                                get("/course-offerings/" + coToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        courseOfferingDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseOfferingDto.class);

        assertEquals(courseOfferingDto.getCourse().getId(), courseDto.getId());
        assertEquals(courseOfferingDto.getYear(), 2021);


        // 5. delete the Course with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/course-offerings/" + courseOfferingDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Courses
        mvcResult =
                mvc.perform(get("/course-offerings").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedCourseOfferings =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CourseOfferingDto[].class));

        assertEquals(returnedCourseOfferings.size(), 0);
    }
}