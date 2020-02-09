package ca.mcgill.cooperator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.Season;
import ca.mcgill.cooperator.service.CoopDetailsService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CoopControllerIT {
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
    
    @Test
    public void testCoopFlow() throws Exception {
    	CoopStatus status = CoopStatus.COMPLETED;
    	CourseDto courseDto = createTestCourse();
    	CourseOfferingDto courseOfferingDto = createTestCourseOffering(courseDto);
    	StudentDto studentDto = createTestStudent();
    	
    	CoopDto coopDto = new CoopDto();
    	coopDto.setStatus(status);
    	coopDto.setCourseOffering(courseOfferingDto);
    	coopDto.setStudent(studentDto);
    	
    	// 1. create the Co-op with a POST request
    	MvcResult mvcResult =
                mvc.perform(
                                post("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
    	
    	// get object from response
        CoopDto returnedCoop =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CoopDto.class);
        assertEquals(returnedCoop.getStatus(), CoopStatus.COMPLETED);
        
        // 2. get the Co-op by ID, valid
        mvc.perform(get("/coops/" + returnedCoop.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        // 3. test getting all Co-ops
        mvcResult =
                mvc.perform(get("/coops").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        // get object from response
        List<CoopDto> returnedCoops =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CoopDto[].class));

        assertEquals(returnedCoops.size(), 1);
        
        CoopDto coopToUpdate = returnedCoop;
        coopToUpdate.setStatus(CoopStatus.FUTURE);
        
        CompanyDto companyDto = createTestCompany();
        EmployerContactDto employerContactDto = createTestEmployerContact(companyDto);
        CoopDetailsDto coopDetailsDto = createTestCoopDetails(coopToUpdate, employerContactDto);
        coopToUpdate.setCoopDetails(coopDetailsDto);
        
        
        // 4. update the co-op with a PUT request
        mvcResult =
                mvc.perform(
                                put("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopToUpdate))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
        
        // get the co-op by ID
        mvcResult =
                mvc.perform(
                                get("/coops/" + coopToUpdate.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        returnedCoop =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CoopDto.class);

        assertEquals(returnedCoop.getStatus(), CoopStatus.FUTURE);
        
        // 5. delete the co-op with a DELETE request
        mvcResult =
                mvc.perform(
                                delete("/coops/" + returnedCoop.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // test getting all co-ops
        mvcResult =
                mvc.perform(get("/coops").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        returnedCoops =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CoopDto[].class));

        assertEquals(returnedCoops.size(), 0);
        
        mvcResult =
                mvc.perform(get("/coop-details").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        List<CoopDetailsDto> coopDetailsDtos =
                Arrays.asList(
                        objectMapper.readValue(
                                mvcResult.getResponse().getContentAsString(), CoopDetailsDto[].class));

        assertEquals(coopDetailsDtos.size(), 0);
    }
    
    public CourseDto createTestCourse() throws Exception {
    	CourseDto courseDto = new CourseDto();
    	courseDto.setName("FACC200");
    	MvcResult mvcResult =
                mvc.perform(
                                post("/courses")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(courseDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        courseDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CourseDto.class);
        
    	
    	return courseDto;
    }
    
    public CourseOfferingDto createTestCourseOffering(CourseDto courseDto) throws Exception{
    	CourseOfferingDto courseOfferingDto = new CourseOfferingDto();
    	courseOfferingDto.setYear(2020);
    	courseOfferingDto.setSeason(Season.FALL);
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
    	
    	return courseOfferingDto;
    }
    
    public StudentDto createTestStudent() throws Exception{
    	StudentDto studentDto = new StudentDto();
    	studentDto.setEmail("susan@gmail.com");
    	studentDto.setFirstName("Susan");
    	studentDto.setLastName("Matuszewski");
    	studentDto.setStudentId("12345678");
    	MvcResult mvcResult =
                mvc.perform(
                                post("/students")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(studentDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        studentDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), StudentDto.class);
        
        return studentDto;
    	
    }
    
    public CoopDetailsDto createTestCoopDetails(CoopDto coopDto, EmployerContactDto employerContactDto) throws Exception {
    	CoopDetailsDto coopDetailsDto = new CoopDetailsDto();
    	coopDetailsDto.setPayPerHour(20);
    	coopDetailsDto.setHoursPerWeek(40);
    	coopDetailsDto.setCoop(coopDto);
    	coopDetailsDto.setEmployerContact(employerContactDto);
    	
    	MvcResult mvcResult =
                mvc.perform(
                                post("/coop-details")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDetailsDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();
    	// get object from response
        coopDetailsDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CoopDetailsDto.class);
    	
    	return coopDetailsDto;
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
        companyDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), CompanyDto.class);
        
        return companyDto;
    	
    }
    
    public EmployerContactDto createTestEmployerContact(CompanyDto companyDto) throws Exception{
    	EmployerContactDto employerContactDto = new EmployerContactDto();
    	employerContactDto.setFirstName("Emma");
    	employerContactDto.setLastName("Eags");
    	employerContactDto.setEmail("emma@gmail.com");
    	employerContactDto.setPhoneNumber("213435566");
    	employerContactDto.setCompany(companyDto);
    	
    	MvcResult mvcResult =
                mvc.perform(
                                post("/employer-contacts")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(employerContactDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
    	employerContactDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);
    	
    	return employerContactDto;
    }
}
