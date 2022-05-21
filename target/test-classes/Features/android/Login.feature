Feature: [Login] Login Test cases

@TestCaseId=TOR2D-XXXX
@Login
@Regression
Scenario Outline: Verify login with positive data
Given User open the url
And User enter username <username> and password <password>
Then User logged in successfully
When User send request call
Then User got response
And Response code is 200
  Examples:
    | username  | password  |
    | user      | password  |
    | user      | password  |
    | user      | password  |