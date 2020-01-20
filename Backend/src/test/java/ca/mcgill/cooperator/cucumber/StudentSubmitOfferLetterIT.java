package ca.mcgill.cooperator.cucumber;

import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class StudentSubmitOfferLetterIT {

    @When("the Student uploads a copy of their offer letter")
    public void studentSubmitsOfferLetter() {
        assertTrue(true);
    }

    @And("submits the details of their Coop term")
    public void studentSubmitsCoopDetails() {
        assertTrue(true);
    }

    @Then("the offer letter is put up for review by an Admin")
    public void offerLetterIsPutUpForReview() {
        assertTrue(true);
    }
}
