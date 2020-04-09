package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.AuthorRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.StudentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class NotificationControllerIT extends BaseControllerIT {
    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired NotificationRepository notificationRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired AdminRepository adminRepository;
    @Autowired AuthorRepository authorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        authorRepository.deleteAll();
        studentRepository.deleteAll();
        adminRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    public void testNotificationFlow() throws Exception {
        StudentDto studentDto = createTestStudent();
        AdminDto adminDto = createTestAdmin();
        NotificationDto notifDto = new NotificationDto();

        String title = "Hello";
        String body = "Please attend meeting.";
        notifDto.setBody(body);
        notifDto.setTitle(title);
        notifDto.setStudent(studentDto);
        notifDto.setSender(adminDto);

        // 1. create notification

        MvcResult mvcResult =
                mvc.perform(
                                post("/notifications")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(notifDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        notifDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), NotificationDto.class);

        assertEquals(title, notifDto.getTitle());

        mvc.perform(
                        get("/notifications/" + notifDto.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvcResult =
                mvc.perform(get("/notifications").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<NotificationDto> notifDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                NotificationDto[].class));

        assertEquals(1, notifDtos.size());

        // 2. create 2nd notification

        NotificationDto notifDto1 = new NotificationDto();

        title = "Bye";
        body = "Please attend meeting again.";
        notifDto1.setBody(body);
        notifDto1.setTitle(title);
        notifDto1.setStudent(studentDto);
        notifDto1.setSender(adminDto);

        mvcResult =
                mvc.perform(
                                post("/notifications")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(notifDto1))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        notifDto1 =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), NotificationDto.class);

        assertEquals(title, notifDto1.getTitle());

        mvc.perform(
                        get("/notifications/" + notifDto1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvcResult =
                mvc.perform(get("/notifications").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        notifDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                NotificationDto[].class));

        assertEquals(2, notifDtos.size());

        mvcResult =
                mvc.perform(
                                get("/students/id/" + studentDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        studentDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentDto.class);

        assertEquals(2, studentDto.getNotifications().size());

        // 3. update one notification

        title = "Hello again";

        notifDto1.setTitle(title);

        mvcResult =
                mvc.perform(
                                put("/notifications/" + notifDto1.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(notifDto1))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        notifDto1 =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), NotificationDto.class);

        assertEquals(title, notifDto1.getTitle());

        // 4. delete both notifications

        mvc.perform(
                        delete("/notifications/" + notifDto1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(notifDto1))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        mvc.perform(
                        delete("/notifications/" + notifDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(notifDto1))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        mvcResult =
                mvc.perform(get("/notifications").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        notifDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                NotificationDto[].class));

        assertEquals(0, notifDtos.size());
    }
}
