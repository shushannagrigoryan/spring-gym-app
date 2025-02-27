Feature: Creating a new training

  @createUser
  @auth
  Scenario: Successfully add a training
    Given an authorized user given the traineeUsername, trainerUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When the user sends a POST request to create a new training
    Then the create training response status should be 200
    And the response for creating a new training should contain the message "Successfully created a new training."

  @createUser
  @auth
  Scenario: Fail to create a new training (invalid input data)
    Given an authorized user given the traineeUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When the user sends a POST request to create a new training without trainerUsername
    Then the create training response status should be 400
    And the response for creating a new training should contain an error message "Trainer username is required."


  Scenario: Fail to create a new training (fail auth)
    Given an unauthorized user given the traineeUsername, trainerUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When an unauthorized user sends a POST request to create a new training
    Then the create training response status should be 401
    And the response for creating a new training should contain the message "Authentication failed: Full authentication is required to access this resource"