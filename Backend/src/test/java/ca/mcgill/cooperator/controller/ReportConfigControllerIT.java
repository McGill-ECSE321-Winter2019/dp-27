package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dto.ReportConfigDto;
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
public class ReportConfigControllerIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired ReportConfigRepository reportConfigRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reportConfigRepository.deleteAll();
    }

    @Test
    public void testReportConfigFlow() throws Exception {
        ReportConfigDto rcDto = new ReportConfigDto();
        rcDto.setDeadline(14);
        rcDto.setRequiresFile(true);
        rcDto.setIsDeadlineFromStart(true);
        rcDto.setType("First Evaluation");

        // 1. create the ReportConfig with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/report-configs")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rcDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get created ReportConfig from response
        rcDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportConfigDto.class);

        // 2. get the ReportConfig by ID, valid
        mvc.perform(get("/report-configs/" + rcDto.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all ReportConfigs
        mvcResult =
                mvc.perform(get("/report-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        List<ReportConfigDto> rcDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportConfigDto[].class));

        assertEquals(1, rcDtos.size());

        // 4. update the ReportConfig with a PUT request
        rcDto.setType("Second Evaluation");
        rcDto.setRequiresFile(false);

        mvc.perform(
                        put("/report-configs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rcDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        // get the ReportConfig by ID
        mvcResult =
                mvc.perform(
                                get("/report-configs/" + rcDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        rcDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportConfigDto.class);

        // these fields should have updated
        assertEquals("Second Evaluation", rcDto.getType());
        assertFalse(rcDto.getRequiresFile());
        // these fields should be unchanged
        assertTrue(rcDto.getIsDeadlineFromStart());
        assertEquals(14, rcDto.getDeadline());

        // 5. delete the ReportConfig with a DELETE request
        mvc.perform(
                        delete("/report-configs/" + rcDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        // 6. check that there are now zero ReportConfigs
        mvcResult =
                mvc.perform(get("/report-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        rcDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportConfigDto[].class));

        assertEquals(0, rcDtos.size());
    }

    @Test
    public void testInvalidReportConfigFlow() throws Exception {
        ReportConfigDto rcDto = new ReportConfigDto();
        rcDto.setDeadline(-1);
        rcDto.setType("  ");

        // 1. invalid create: invalid fields
        mvc.perform(
                        post("/report-configs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rcDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());

        rcDto.setDeadline(14);
        rcDto.setRequiresFile(true);
        rcDto.setIsDeadlineFromStart(true);
        rcDto.setType("First Evaluation");

        // valid create so that we have a ReportConfig to work with
        mvc.perform(
                        post("/report-configs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rcDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        rcDto.setType("");
        rcDto.setDeadline(-1);

        // 2. invalid update: invalid fields and no ID provided
        mvc.perform(
                        put("/report-configs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rcDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());

        // get the created ReportConfig
        MvcResult mvcResult =
                mvc.perform(get("/report-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        List<ReportConfigDto> rcDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportConfigDto[].class));

        rcDto = rcDtos.get(0);

        // 3. invalid delete: wrong ID
        mvc.perform(
                        delete("/report-configs/" + (rcDto.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());
    }
}
