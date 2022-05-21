package org.tora.stepDefinitions.android;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.tora.generic.BaseAll;

public class LoginSteps extends BaseAll {

    @Given("^User open the url$")
    public void user_open_the_url() {
        System.out.println("User opened url");
    }

    @And("^User enter username (.*) and password (.*)$")
    public void user_open_the_url(String username, String password) {
        System.out.println(username);
        System.out.println(password);
    }

    @And("^User logged in successfully$")
    public void user_logged_in_successfully(String username, String password) {
        System.out.println(username);
        System.out.println(password);
    }


}
