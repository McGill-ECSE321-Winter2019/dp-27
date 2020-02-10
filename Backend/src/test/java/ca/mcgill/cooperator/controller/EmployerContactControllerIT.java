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
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.EmployerReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashSet;
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

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.EmployerReportRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.model.EmployerContact;
import ca.mcgill.cooperator.model.EmployerReport;
import ca.mcgill.cooperator.service.EmployerContactService;
import ca.mcgill.cooperator.service.EmployerReportService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployerContactControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;


    @Autowired private EmployerContactService employerContactService;
    @Autowired private EmployerReportService employerReportService;

    
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired EmployerReportRepository employerReportRepository;


    @BeforeEach @AfterEach
    public void clearDatabase() {
  	List<EmployerReport> ecs = employerReportService.getAllEmployerReports();
      for (EmployerReport ec : ecs) {
      	ec.setEmployerContact(null);
      	employerReportRepository.save(ec);
      }
      employerReportRepository.deleteAll();
    	employerContactRepository.deleteAll();
    	companyRepository.deleteAll();
    }

    /**
     * Tests creating, reading, updating and deleting an Employer Contact
     *
     * @throws Exception
     */
    @Test
    public void testEmployerContactFlow() throws Exception {
        String email = "susan@gmail.com";
        String firstName = "Susan";
        String lastName = "Matuszewski";
        String phoneNumber = "123456789";
        CompanyDto companyDto = createTestCompany();
        EmployerContactDto testEmployerContact = new EmployerContactDto();
        testEmployerContact.setFirstName(firstName);
        testEmployerContact.setLastName(lastName);
        testEmployerContact.setEmail(email);
        testEmployerContact.setPhoneNumber(phoneNumber);
        testEmployerContact.setCompany(companyDto);

        // 1. create the Employer Contact with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/employer-contacts")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        testEmployerContact))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        EmployerContactDto returnedEmployerContact =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);
        assertEquals(returnedEmployerContact.getEmail(), "susan@gmail.com");

        // 2. get the Employer Contact by ID, valid
        mvc.perform(
                        get("/employer-contacts/" + returnedEmployerContact.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all Employer Contact
        mvcResult =
                mvc.perform(get("/employer-contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<EmployerContactDto> returnedEmployerContacts =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                EmployerContactDto[].class));

        assertEquals(returnedEmployerContacts.size(), 1);

        EmployerContactDto employerContactToUpdate = returnedEmployerContacts.get(0);
        employerContactToUpdate.setFirstName("Paul");
        employerContactToUpdate.setLastName("Hooli");
        employerContactToUpdate.setEmail("testemail2@gmail.com");

        // 4. update the Employer Contact with a PUT request
        mvcResult =
                mvc.perform(
                                put("/employer-contacts")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        employerContactToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedEmployerContact =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);

        mvcResult =
                mvc.perform(get("/employer-contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get the Employer Contact by ID
        mvcResult =
                mvc.perform(
                                get("/employer-contacts/" + employerContactToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        returnedEmployerContact =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);

        assertEquals(returnedEmployerContact.getFirstName(), "Paul");
        assertEquals(returnedEmployerContact.getLastName(), "Hooli");
        assertEquals(returnedEmployerContact.getEmail(), "testemail2@gmail.com");

        // 5. delete the Employer Contact with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/employer-contacts/" + returnedEmployerContact.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Employer Contacts
        mvcResult =
                mvc.perform(get("/employer-contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedEmployerContacts =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                EmployerContactDto[].class));

        assertEquals(returnedEmployerContacts.size(), 0);
    }

    public CompanyDto createTestCompany() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Cisco");
        companyDto.setCity("Ottawa");
        companyDto.setRegion("Ontario");
        companyDto.setCountry("Canada");

        MvcResult mvcResult =
                mvc.perform(
                                post("/companies")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(companyDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        CompanyDto returnedCompanyDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CompanyDto.class);

        return returnedCompanyDto;
    }
}
