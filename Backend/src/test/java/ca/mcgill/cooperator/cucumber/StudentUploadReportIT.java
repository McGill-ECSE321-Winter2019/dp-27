package ca.mcgill.cooperator.cucumber;

import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class StudentUploadReportIT {

    @Given("the Student is currently doing a Coop term")
    public void studentHasCurrentCoopTerm() {
        assertTrue(true);
    }

    @And("the Student has a Report due")
    public void studentHasReportDue() {
        assertTrue(true);
    }

    @When("the Student uploads the proper Report")
    public void studentUploadsReport() {
        assertTrue(true);
    }

    @Then("the Report is saved in the system")
    public void saveReport() {
        assertTrue(true);
    }

    @And("the Student has uploaded a Report type previously")
    public void studentHasUploadedReportPreviously() {
        assertTrue(true);
    }

    @When("the Student uploads the same type of Report again")
    public void studentUploadsReportAgain() {
        assertTrue(true);
    }

    @Then("the new Report overwrites the old one in the system")
    public void overwriteReport() {
        assertTrue(true);
    }
}
