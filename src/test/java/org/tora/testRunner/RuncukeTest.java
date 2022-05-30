package org.tora.testRunner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions
        (
                features = "src/test/resources/Features",
                glue = "org.tora",
                dryRun = false,
                monochrome = true,
                tags = "@Vishnu",
                plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber.json", "junit:target/cucumber.xml"}
        )
public class RuncukeTest extends AbstractTestNGCucumberTests {
}
