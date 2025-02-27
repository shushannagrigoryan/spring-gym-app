package org.example.integration.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/featureFiles/ITFeatures"},
    plugin = {"pretty"},
    glue = {"org.example.integration.steps", "org.example.integration.config"}
)
public class TestRunnerIT {
}
