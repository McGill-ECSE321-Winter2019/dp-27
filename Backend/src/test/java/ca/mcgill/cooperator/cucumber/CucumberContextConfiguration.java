package ca.mcgill.cooperator.cucumber;

import io.cucumber.java.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CucumberContextConfiguration {

    @Before
    public void setupCucumberSpringContext() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
}
