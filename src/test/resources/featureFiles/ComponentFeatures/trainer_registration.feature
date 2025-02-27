Feature: Trainer Registration

  Scenario: Successfully registering a new trainer
    Given a trainer with first name "A" and last name "B" and specialization "1"
    When the trainer submits a registration request
    Then the response status should be 201
    And the response should contain a generated username and password

  Scenario: Registering a trainer with missing firstName
    Given a trainer with an empty first name
    When the trainer submits a registration request
    Then the response status should be 400
    And the response should contain an error message "FirstName is required and can't be blank"

  Scenario: Registering a trainer with missing specialization
    Given a trainer with first name "A" and last name "B" and missing specialization
    When the trainer submits a registration request
    Then the response status should be 400
    And the response should contain an error message "Specialization is required."

  Scenario: Registering a trainer with invalid specialization
    Given a trainer with first name "A" and last name "B" and invalid specialization "-1"
    When the trainer submits a registration request
    Then the response status should be 400
    And the response should contain an error message "Specialization must be greater than or equal to 1"