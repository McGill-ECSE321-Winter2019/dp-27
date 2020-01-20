package ca.mcgill.cooperator.cucumber;

import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class StudentGetCoopInfoIT {

    @Given("the Student has at least one current or previous Coop term")
    public void studentHasAtLeastOneCoopTerm() {
        assertTrue(true);
    }

    @When("the Student requests to get their Coop term information")
    public void studentRequestsCoopInformation() {
        assertTrue(true);
    }

    @Then("the system returns the information for all of their Coop terms")
    public void getCoopInformationForStudent() {
        assertTrue(true);
    }
}
