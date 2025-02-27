Feature: Trainee Registration

  Scenario: Successfully registering a new trainee
    Given a trainee with first name "A" and last name "B"
    When the trainee submits a registration request
    Then the traineeRegistration response status should be 201
    And the traineeRegistration response should contain a generated username and password

  Scenario: Registering a trainee with missing required fields
    Given a trainee with an empty first name
    When the trainee submits a registration request
    Then the traineeRegistration response status should be 400
    And the traineeRegistration response should contain an error message "FirstName is required and can't be blank"
