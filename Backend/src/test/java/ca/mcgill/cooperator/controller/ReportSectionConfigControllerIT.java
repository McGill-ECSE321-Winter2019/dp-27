package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.ReportConfigRepository;
import ca.mcgill.cooperator.dao.ReportSectionConfigRepository;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.model.ReportResponseType;
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
public class ReportSectionConfigControllerIT extends BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired ReportConfigRepository reportConfigRepository;
    @Autowired ReportSectionConfigRepository reportSectionConfigRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reportConfigRepository.deleteAll();
        reportSectionConfigRepository.deleteAll();
    }

    @Test
    public void testReportSectionConfigFlow() throws Exception {
        String prompt = "How was your co-op?";

        ReportConfigDto rcDto = new ReportConfigDto();
        rcDto.setDeadline(14);
        rcDto.setRequiresFile(true);
        rcDto.setIsDeadlineFromStart(true);
        rcDto.setType("First Evaluation");

        // 1. create a ReportConfig with a POST request
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

        ReportSectionConfigDto rscDto = new ReportSectionConfigDto();
        rscDto.setSectionPrompt(prompt);
        rscDto.setResponseType(ReportResponseType.LONG_TEXT);
        rscDto.setReportConfig(rcDto);
        rscDto.setQuestionNumber(12);

        // 2. create the ReportSectionConfig with a POST request
        mvcResult =
                mvc.perform(
                                post("/report-section-configs")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rscDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get created ReportSectionConfig from response
        rscDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportSectionConfigDto.class);

        // 3. get the ReportSectionConfig by ID, valid
        mvc.perform(
                        get("/report-section-configs/" + rscDto.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 4. test getting all ReportSectionConfigs
        mvcResult =
                mvc.perform(get("/report-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        List<ReportSectionConfigDto> rscDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportSectionConfigDto[].class));

        assertEquals(1, rscDtos.size());

        // 5. update the ReportSectionConfig with a PUT request
        rscDto.setSectionPrompt("Rate your co-op from 1-10.");
        rscDto.setResponseType(ReportResponseType.NUMBER);

        mvc.perform(
                        put("/report-section-configs/" + rscDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rscDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        // get the ReportSectionConfig by ID
        mvcResult =
                mvc.perform(
                                get("/report-section-configs/" + rscDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        rscDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportSectionConfigDto.class);

        // verify correctness
        assertEquals("Rate your co-op from 1-10.", rscDto.getSectionPrompt());
        assertEquals(ReportResponseType.NUMBER, rscDto.getResponseType());
        // the ReportConfig should be unchanged
        assertEquals(rcDto.getId(), rscDto.getReportConfig().getId());

        // 6. delete the ReportSectionConfig with a DELETE request
        mvc.perform(
                        delete("/report-section-configs/" + rscDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().isOk());

        // 7. check that there are now zero ReportSectionConfigs
        mvcResult =
                mvc.perform(get("/report-section-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        rscDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportSectionConfigDto[].class));

        assertEquals(0, rscDtos.size());
    }

    @Test
    public void testInvalidReportSectionConfigFlow() throws Exception {
        ReportSectionConfigDto rscDto = new ReportSectionConfigDto();
        // 1. invalid create: everything is null
        mvc.perform(
                        post("/report-section-configs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rscDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().is5xxServerError());

        ReportConfigDto rcDto = new ReportConfigDto();
        rcDto.setDeadline(14);
        rcDto.setRequiresFile(true);
        rcDto.setIsDeadlineFromStart(true);
        rcDto.setType("First Evaluation");

        // create a ReportConfig
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

        String prompt = "How was your co-op?";
        rscDto.setSectionPrompt(prompt);
        rscDto.setResponseType(ReportResponseType.LONG_TEXT);
        rscDto.setReportConfig(rcDto);
        rscDto.setQuestionNumber(3);

        // valid create so that we have a ReportSectionConfig to work with
        mvcResult =
                mvc.perform(
                                post("/report-section-configs")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rscDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        rscDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportSectionConfigDto.class);

        rscDto.setSectionPrompt(" ");

        // 2. invalid update: empty prompt, expect null pointer exception
        mvc.perform(
                        put("/report-section-configs/" + rscDto.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rscDto))
                                .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError());

        // get the created ReportSectionConfig
        mvcResult =
                mvc.perform(get("/report-section-configs").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        List<ReportSectionConfigDto> rscDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                ReportSectionConfigDto[].class));

        rscDto = rscDtos.get(0);

        // 3. invalid delete: wrong ID
        mvc.perform(
                        delete("/report-section-configs/" + (rscDto.getId() + 1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError());
    }
}
