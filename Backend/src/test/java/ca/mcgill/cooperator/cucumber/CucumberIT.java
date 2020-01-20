package ca.mcgill.cooperator.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Configuration for Cucumber tests
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", plugin = {"pretty", "json:target/cucumber-report.json"})
public class CucumberIT {

}
