package org.example.component.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/featureFiles/ComponentFeatures"},
    plugin = {"pretty"},
    glue = {"org.example.component.steps", "org.example.component.config"}
)
public class TestRunner {
}
