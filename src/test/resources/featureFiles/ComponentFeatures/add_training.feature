Feature: Creating a new training

  @createUser
  @auth
  Scenario: Successfully add a training
    Given an authorized user given the traineeUsername, trainerUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When the user sends a POST request to create a new training
    Then the create training response status should be 200
    And the response for creating a new training should contain the message "Successfully created a new training."

  Scenario: Fail to create a new training (fail auth)
    Given an unauthorized user given the traineeUsername, trainerUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When an unauthorized user sends a POST request to create a new training
    Then the create training response status should be 401
    And the response for creating a new training should contain the message "Authentication failed: Full authentication is required to access this resource"

  @createUser
  @auth
  Scenario: Fail to create a new training (TrainerWorkloadService is not responding)
    Given an authorized user given the traineeUsername, trainerUsername, trainingName "trainingName", trainingDate "2025-07-07T14:00", trainingDuration "60"
    When the user sends a POST request to create a new training, the trainerWorkloadService is not responding
    Then the create training response status should be 500
    And the response for creating a new training should contain an error message "INTERNAL_SERVER_ERROR"