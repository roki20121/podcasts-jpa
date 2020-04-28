package com.roman.podcastplayer;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "features", tags = "@Smoke")
public class RunCucumberSmokeTest {

}
