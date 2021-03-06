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
public class CourseControllerIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired CourseRepository courseRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
        courseOfferingRepository.deleteAll();
    }

    /**
     * Tests creating, reading, updating and deleting a course
     *
     * @throws Exception
     */
    @Test
    public void testCourseFlow() throws Exception {
        CourseDto testCourse = new CourseDto(1, "ECSE223", new ArrayList<CourseOfferingDto>());

        // 1. create the Course with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/courses")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(testCourse))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        CourseDto returnedCourse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseDto.class);
        assertEquals(returnedCourse.getName(), "ECSE223");

        // 2. get the course by ID, valid
        mvc.perform(
                        get("/courses/" + returnedCourse.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all Courses
        mvcResult =
                mvc.perform(get("/courses").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<CourseDto> returnedCourses =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CourseDto[].class));

        assertEquals(returnedCourses.size(), 1);

        CourseDto courseToUpdate = returnedCourses.get(0);

        courseToUpdate.setName("ECSE321");
        List<CourseOfferingDto> coDtos = new ArrayList<>();
        CourseOfferingDto coDto = createTestCourseOffering(courseToUpdate);
        coDtos.add(coDto);
        courseToUpdate.setCourseOfferings(coDtos);

        // 4. update the Course with a PUT request
        mvcResult =
                mvc.perform(
                                put("/courses/" + courseToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(courseToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get the course by ID
        mvcResult =
                mvc.perform(
                                get("/courses/" + courseToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedCourse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseDto.class);

        assertEquals(returnedCourse.getName(), "ECSE321");
        assertEquals(returnedCourse.getCourseOfferings().size(), 1);
        assertEquals(returnedCourse.getCourseOfferings().get(0).getId(), coDto.getId());

        // 5. delete the Course with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/courses/" + returnedCourse.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Courses
        mvcResult =
                mvc.perform(get("/courses").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedCourses =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CourseDto[].class));

        assertEquals(returnedCourses.size(), 0);
    }

    @Test
    public void testInvalidCourseFlow() throws Exception {
        CourseDto invalidCourse = new CourseDto(1, "", null);
        CourseDto testCourse = new CourseDto(1, "Test", null);

        // 1. invalid create
        mvc.perform(
                        post("/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidCourse))
                                .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError());

        // create the Course with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/courses")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(testCourse))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        CourseDto returnedCourse =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseDto.class);

        // 2. get the Course by ID, invalid
        mvc.perform(
                        get("/courses/" + (returnedCourse.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        returnedCourse.setName("");

        // 3. invalid update
        mvc.perform(
                        put("/courses/" + returnedCourse.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(returnedCourse))
                                .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError());

        // 4. invalid delete
        mvc.perform(
                        delete("/courses/" + (returnedCourse.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError());
    }
}
