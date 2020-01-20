package ca.mcgill.cooperator.cucumber;

import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class AdminApproveCoopIT {

    @When("the Student submits their offer letter")
    public void studentSubmitsOfferLetter() {
        assertTrue(true);
    }

    @Then("the Admin approves the Coop term")
    public void adminApprovesCoopTerm() {
        assertTrue(true);
    }

    @Then("the Admin rejects the Coop term")
    public void adminRejectsCoopTerm() {
        assertTrue(true);
    }
}
