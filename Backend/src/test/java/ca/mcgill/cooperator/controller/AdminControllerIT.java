package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.AdminRepository;
import ca.mcgill.cooperator.dao.NotificationRepository;
import ca.mcgill.cooperator.dto.AdminDto;
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
public class AdminControllerIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired AdminRepository adminRepository;
    @Autowired NotificationRepository notificationRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        adminRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    /**
     * Tests creating, reading, updating and deleting an Admin
     *
     * @throws Exception
     */
    @Test
    public void testAdminFlow() throws Exception {
        AdminDto testAdmin = new AdminDto(1, "Test", "Admin", "test@gmail.com", null);

        // 1. create the Admin with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/admins")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(testAdmin))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        AdminDto returnedAdmin =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), AdminDto.class);
        assertEquals(returnedAdmin.getEmail(), "test@gmail.com");

        // 2. get the Admin by ID, valid
        mvc.perform(get("/admins/" + returnedAdmin.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all Admins
        mvcResult =
                mvc.perform(get("/admins").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<AdminDto> returnedAdmins =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), AdminDto[].class));

        assertEquals(returnedAdmins.size(), 1);

        AdminDto adminToUpdate = returnedAdmins.get(0);
        adminToUpdate.setFirstName("Paul");
        adminToUpdate.setLastName("Hooli");
        adminToUpdate.setEmail("testemail2@gmail.com");

        // 4. update the Admin with a PUT request
        mvcResult =
                mvc.perform(
                                put("/admins/" + adminToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(adminToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get the Admin by ID
        mvcResult =
                mvc.perform(
                                get("/admins/" + adminToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedAdmin =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), AdminDto.class);

        assertEquals(returnedAdmin.getFirstName(), "Paul");
        assertEquals(returnedAdmin.getLastName(), "Hooli");
        assertEquals(returnedAdmin.getEmail(), "testemail2@gmail.com");

        // 5. delete the Admin with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/admins/" + returnedAdmin.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Admins
        mvcResult =
                mvc.perform(get("/admins").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedAdmins =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), AdminDto[].class));

        assertEquals(returnedAdmins.size(), 0);
    }

    @Test
    public void testInvalidAdminFlow() throws Exception {
        AdminDto invalidAdmin = new AdminDto(1, "", "", "", null);
        AdminDto testAdmin = new AdminDto(1, "Test", "Admin", "test@gmail.com", null);

        // 1. invalid create
        mvc.perform(
                        post("/admins")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidAdmin))
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());

        // create the Admin with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/admins")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(testAdmin))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        AdminDto returnedAdmin =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), AdminDto.class);

        // 2. get the Admin by ID, invalid
        mvc.perform(
                        get("/admins/" + (returnedAdmin.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        returnedAdmin.setFirstName("");
        returnedAdmin.setLastName("");
        returnedAdmin.setEmail("");

        // 3. invalid update
        mvc.perform(
                        put("/admins/" + returnedAdmin.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(returnedAdmin))
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());

        // 4. invalid delete
        mvc.perform(
                        delete("/admins/" + (returnedAdmin.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());
    }
}
