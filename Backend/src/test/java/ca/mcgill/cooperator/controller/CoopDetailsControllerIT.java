package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dao.CompanyRepository;
import ca.mcgill.cooperator.dao.CoopDetailsRepository;
import ca.mcgill.cooperator.dao.CoopRepository;
import ca.mcgill.cooperator.dao.CourseOfferingRepository;
import ca.mcgill.cooperator.dao.CourseRepository;
import ca.mcgill.cooperator.dao.EmployerContactRepository;
import ca.mcgill.cooperator.dao.StudentRepository;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.model.CoopDetails;
import ca.mcgill.cooperator.service.CoopDetailsService;
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
public class CoopDetailsControllerIT extends ControllerIT {
    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired CoopRepository coopRepository;
    @Autowired CourseOfferingRepository courseOfferingRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired CoopDetailsRepository coopDetailsRepository;
    @Autowired EmployerContactRepository employerContactRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired CoopDetailsService coopDetailsService;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        List<CoopDetails> coopDetails = coopDetailsService.getAllCoopDetails();
        for (CoopDetails cd : coopDetails) {
            cd.setCoop(null);
            coopDetailsRepository.save(cd);
        }
        coopDetailsRepository.deleteAll();
        courseOfferingRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        coopRepository.deleteAll();
        coopDetailsRepository.deleteAll();
        employerContactRepository.deleteAll();
        companyRepository.deleteAll();
    }

    /**
     * Tests creating CoopDetails: 
     * 1. creates coop details 
     * 2. gets created coop details by id 
     * 3. get all coop details 
     * 4. update coop details 
     * 5. delete coop details
     *
     * @throws Exception
     */
    @Test
    public void testCoopDetailsFlow() throws Exception {
        CourseDto courseDto = createTestCourse();
        CourseOfferingDto courseOfferingDto = createTestCourseOffering(courseDto);
        StudentDto studentDto = createTestStudent();
        CoopDto coopDto = createTestCoop(courseOfferingDto, studentDto);
        CompanyDto companyDto = createTestCompany();
        EmployerContactDto employerContactDto = createTestEmployerContact(companyDto);

        CoopDetailsDto coopDetailsDto = new CoopDetailsDto();
        coopDetailsDto.setCoop(coopDto);
        coopDetailsDto.setEmployerContact(employerContactDto);
        coopDetailsDto.setHoursPerWeek(40);
        coopDetailsDto.setPayPerHour(300);

        // 1. create the Co-op Details with a POST request
        MvcResult mvcResult =
                mvc.perform(
                                post("/coop-details")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDetailsDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        CoopDetailsDto returnedCoopDetails =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CoopDetailsDto.class);
        assertEquals(returnedCoopDetails.getPayPerHour(), 300);

        // 2. get the Co-op Details by ID, valid
        mvc.perform(
                        get("/coop-details/" + returnedCoopDetails.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. test getting all Co-op Details
        mvcResult =
                mvc.perform(get("/coop-details").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        // get object from response
        List<CoopDetailsDto> returnedCoopDetailsList =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                CoopDetailsDto[].class));

        assertEquals(returnedCoopDetailsList.size(), 1);

        CoopDetailsDto coopDetailsToUpdate = returnedCoopDetails;
        coopDetailsToUpdate.setPayPerHour(500);

        // 4. update the Co-op Details with a PUT request
        mvcResult =
                mvc.perform(
                                put("/coop-details")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(
                                                        coopDetailsToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get the Co-op Details by ID
        mvcResult =
                mvc.perform(
                                get("/coop-details/" + coopDetailsToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        returnedCoopDetails =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CoopDetailsDto.class);

        assertEquals(returnedCoopDetails.getPayPerHour(), 500);

        // 5. delete the Co-op Details with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/coop-details/" + returnedCoopDetails.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all Co-op Details
        mvcResult =
                mvc.perform(get("/coop-details").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedCoopDetailsList =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(),
                                CoopDetailsDto[].class));

        assertEquals(returnedCoopDetailsList.size(), 0);
    }
}
