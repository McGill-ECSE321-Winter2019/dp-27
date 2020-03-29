package ca.mcgill.cooperator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.mcgill.cooperator.dto.AdminDto;
import ca.mcgill.cooperator.dto.CompanyDto;
import ca.mcgill.cooperator.dto.CoopDetailsDto;
import ca.mcgill.cooperator.dto.CoopDto;
import ca.mcgill.cooperator.dto.CourseDto;
import ca.mcgill.cooperator.dto.CourseOfferingDto;
import ca.mcgill.cooperator.dto.EmployerContactDto;
import ca.mcgill.cooperator.dto.EmployerReportDto;
import ca.mcgill.cooperator.dto.EmployerReportSectionDto;
import ca.mcgill.cooperator.dto.ReportConfigDto;
import ca.mcgill.cooperator.dto.ReportSectionConfigDto;
import ca.mcgill.cooperator.dto.StudentDto;
import ca.mcgill.cooperator.dto.StudentReportDto;
import ca.mcgill.cooperator.dto.StudentReportSectionDto;
import ca.mcgill.cooperator.model.CoopStatus;
import ca.mcgill.cooperator.model.ReportResponseType;
import ca.mcgill.cooperator.model.Season;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * This class contains util methods used by the controller integration tests
 *
 * @see CoopControllerIT (example)
 */
public abstract class BaseControllerIT {

    @Autowired private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    public CourseDto createTestCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("FACC 200");
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

    public CourseDto createTestCourse(String name) throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setName(name);
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

    public CourseOfferingDto createTestCourseOffering(CourseDto courseDto) throws Exception {
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

    public CourseOfferingDto createTestCourseOffering(CourseDto courseDto, int year, Season season)
            throws Exception {
        CourseOfferingDto courseOfferingDto = new CourseOfferingDto();
        courseOfferingDto.setYear(year);
        courseOfferingDto.setSeason(season);
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

    public StudentDto createTestStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setEmail("susan@gmail.com");
        studentDto.setFirstName("Susan");
        studentDto.setLastName("Matuszewski");
        studentDto.setStudentId("123456789");
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

    public CoopDto createTestCoop(
            CourseOfferingDto courseOfferingDto, StudentDto studentDto, CoopStatus coopStatus)
            throws Exception {
        CoopDto coopDto = new CoopDto();
        coopDto.setStatus(coopStatus);
        coopDto.setCourseOffering(courseOfferingDto);
        coopDto.setStudent(studentDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/coops")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(coopDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        coopDto =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CoopDto.class);

        return coopDto;
    }

    public CoopDetailsDto createTestCoopDetails(
            CoopDto coopDto, EmployerContactDto employerContactDto) throws Exception {
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

    public CoopDetailsDto createTestCoopDetails(
            CoopDto coopDto,
            int payPerHour,
            int hoursPerWeek,
            EmployerContactDto employerContactDto)
            throws Exception {
        CoopDetailsDto coopDetailsDto = new CoopDetailsDto();
        coopDetailsDto.setPayPerHour(payPerHour);
        coopDetailsDto.setHoursPerWeek(hoursPerWeek);
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

    public CompanyDto createTestCompany(String name, String city, String region, String country)
            throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(name);
        companyDto.setCity(city);
        companyDto.setRegion(region);
        companyDto.setCountry(country);

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

    public EmployerContactDto createTestEmployerContact(CompanyDto companyDto) throws Exception {
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
                                        .content(
                                                objectMapper.writeValueAsString(employerContactDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        employerContactDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);

        return employerContactDto;
    }

    public EmployerContactDto createTestEmployerContact(
            CompanyDto companyDto,
            String firstName,
            String lastName,
            String email,
            String phoneNumber)
            throws Exception {
        EmployerContactDto employerContactDto = new EmployerContactDto();
        employerContactDto.setFirstName(firstName);
        employerContactDto.setLastName(lastName);
        employerContactDto.setEmail(email);
        employerContactDto.setPhoneNumber(phoneNumber);
        employerContactDto.setCompany(companyDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/employer-contacts")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                objectMapper.writeValueAsString(employerContactDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        employerContactDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), EmployerContactDto.class);

        return employerContactDto;
    }

    public StudentReportSectionDto createTestStudentReportSection(
            ReportSectionConfigDto reportSectionConfigDto, StudentReportDto studentReportDto)
            throws Exception {
        StudentReportSectionDto reportSectionDto = new StudentReportSectionDto();
        reportSectionDto.setResponse("I am a student and this is my response");
        reportSectionDto.setStudentReport(studentReportDto);
        reportSectionDto.setReportSectionConfig(reportSectionConfigDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/student-report-sections")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(reportSectionDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        reportSectionDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        StudentReportSectionDto.class);

        return reportSectionDto;
    }

    public EmployerReportSectionDto createTestEmployerReportSection(
            ReportSectionConfigDto reportSectionConfigDto, EmployerReportDto employerReportDto)
            throws Exception {
        EmployerReportSectionDto reportSectionDto = new EmployerReportSectionDto();
        reportSectionDto.setResponse("I am an employer and this is my response");
        reportSectionDto.setEmployerReport(employerReportDto);
        reportSectionDto.setReportSectionConfig(reportSectionConfigDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/employer-report-sections")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(reportSectionDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        reportSectionDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        EmployerReportSectionDto.class);

        return reportSectionDto;
    }

    public ReportConfigDto createTestReportConfig(
            boolean requiresFile, int deadline, boolean isDeadlineFromStart, String type)
            throws Exception {
        ReportConfigDto rcDto = new ReportConfigDto();
        rcDto.setRequiresFile(requiresFile);
        rcDto.setDeadline(deadline);
        rcDto.setIsDeadlineFromStart(isDeadlineFromStart);
        rcDto.setType(type);

        MvcResult mvcResult =
                mvc.perform(
                                post("/report-configs")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rcDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        rcDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportConfigDto.class);

        return rcDto;
    }

    public ReportSectionConfigDto createTestReportSectionConfig(
            String sectionPrompt, ReportResponseType responseType, ReportConfigDto rcConfigDto)
            throws Exception {
        ReportSectionConfigDto rsConfigDto = new ReportSectionConfigDto();
        rsConfigDto.setSectionPrompt(sectionPrompt);
        rsConfigDto.setResponseType(responseType);
        rsConfigDto.setReportConfig(rcConfigDto);

        MvcResult mvcResult =
                mvc.perform(
                                post("/report-section-configs")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(rsConfigDto))
                                        .characterEncoding("utf-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        // get object from response
        rsConfigDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), ReportSectionConfigDto.class);

        return rsConfigDto;
    }
    
	public AdminDto createTestAdmin() throws Exception {
		AdminDto adminDto = new AdminDto();
		adminDto.setEmail("admin@gmail.com");
		adminDto.setFirstName("Lucy");
		adminDto.setLastName("Brown");
		
		// 1. create the Admin with a POST request
	    MvcResult mvcResult =
	            mvc.perform(
	                            post("/admins")
	                                    .contentType(MediaType.APPLICATION_JSON)
	                                    .content(objectMapper.writeValueAsString(adminDto))
	                                    .characterEncoding("utf-8"))
	                                    .andExpect(status().isOk())
	                                    .andReturn();
	    
        adminDto =
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(), AdminDto.class);
        
        return adminDto;
    	
    }
}
