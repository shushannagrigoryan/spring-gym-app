package org.example.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources"},
    plugin = {"pretty"},
    glue = {"org.example.steps", "org.example.config"}
)
public class TestRunner {
}
