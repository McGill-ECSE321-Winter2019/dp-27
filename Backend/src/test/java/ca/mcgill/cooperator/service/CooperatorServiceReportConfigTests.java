package ca.mcgill.cooperator.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ca.mcgill.cooperator.dao.ReportConfigRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CooperatorServiceReportConfigTests {
	
	@Autowired ReportConfigService reportConfigService;
	
	@Autowired ReportConfigRepository reportConfigRepository;
	
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reportConfigRepository.deleteAll();
    }
    
    @Test
    public void testCreateReportConfig() {
    	
    }
    
    @Test
    public void testCreateReportConfigInvalid() {
    	
    }
    
    @Test
    public void testUpdateReportConfig() {
    	
    }
    
    @Test
    public void testUpdateReportConfigInvalid() {
    	
    }
    
    @Test
    public void testDeleteReportConfig() {
    	
    }
    
    @Test 
    public void testDeleteReportConfigInvalid() {
    	
    }

}
