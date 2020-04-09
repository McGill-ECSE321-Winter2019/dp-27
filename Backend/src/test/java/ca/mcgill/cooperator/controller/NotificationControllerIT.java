package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.NotificationDto;
import ca.mcgill.cooperator.dto.StudentDto;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NotificationControllerIT extends BaseControllerIT {
	@Autowired private MockMvc mvc;

	@Autowired private ObjectMapper objectMapper;
	
	@Autowired NotificationRepository notificationRepository;
	@Autowired StudentRepository studentRepository;
	@Autowired AdminRepository adminRepository;
	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
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
		
		
        MvcResult mvcResult =
                mvc.perform(
                                post("/notifications")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(notifDto))
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
                                        .content(
                                                objectMapper.writeValueAsString(notifDto1))
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
		        mvc.perform(get("/students/" + studentDto.getId()).contentType(MediaType.APPLICATION_JSON))
		                .andExpect(status().isOk())
		                .andReturn();
		
		// get object from response
		studentDto =
	                objectMapper.readValue(
	                        mvcResult.getResponse().getContentAsString(),
	                        StudentDto.class);
		
		assertEquals(2, studentDto.getNotifications().size());
		
		
		
	}

}