package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
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
public class CompanyControllerIT extends ControllerIT {
    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    public void testCoopDetailsFlow() throws Exception {

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Facebook");
        companyDto.setCity("Seattle");
        companyDto.setRegion("Washington");
        companyDto.setCountry("United States");

        // 1. create the Co-op Details with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/companies")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(companyDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        companyDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CompanyDto.class);
        assertEquals(companyDto.getName(), "Facebook");

        // 2. get the Co-op Details by ID, valid
        mvc.perform(get("/companies/" + companyDto.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all Co-op Details
        mvcResult =
                mvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        // get object from response
        List<CompanyDto> companyDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CompanyDto[].class));

        assertEquals(companyDtos.size(), 1);

        List<EmployerContactDto> employerContactDtos = new ArrayList<EmployerContactDto>();
        EmployerContactDto employerContactDto = createTestEmployerContact(companyDto);
        employerContactDtos.add(employerContactDto);
        companyDto.setEmployees(employerContactDtos);
        companyDto.setName("Cisco");

        // 4. update the Co-op Details with a PUT request
        mvcResult =
                mvc.perform(
                                put("/companies")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(companyDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        companyDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CompanyDto.class);

        assertEquals(companyDto.getName(), "Cisco");

        assertEquals(companyDto.getEmployees().size(), 1);

        // 5. delete the Co-op Details with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/companies/" + companyDto.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Co-op Details
        mvcResult =
                mvc.perform(get("/companies").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        companyDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CompanyDto[].class));

        assertEquals(companyDtos.size(), 0);

        // test getting all Co-op Details
        mvcResult =
                mvc.perform(get("/employer-contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        employerContactDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                EmployerContactDto[].class));

        assertEquals(employerContactDtos.size(), 0);
    }
}
