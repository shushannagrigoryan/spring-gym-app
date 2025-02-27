Feature: Retrieve trainer's workload

  @createUser
  @auth
  Scenario: Successfully retrieving trainer's workload
    Given an authorized user and given the trainerUsername, trainingYear "2025" and trainingMonth "6"
    When the user sends a GET request to retrieve trainer's workload
    Then the response status for getting trainer's workload should be 200
    And the response for getting trainer's workload should contain trainer's username, year, month, and workload
    And the response for getting trainer's workload should contain the message "Successfully retrieved trainer's workload"


  Scenario: Fail to retrieve trainer's workload (fail auth)
    Given an unauthorized user and given the trainerUsername, trainingYear "2025" and trainingMonth "6"
    When an unauthorized user sends a POST request to retrieve trainer's workload
    Then the response status for getting trainer's workload should be 401
    And the response for getting trainer's workload should contain the error message "Authentication failed: Full authentication is required to access this resource"

  @createUser
  @auth
  Scenario: Fail to retrieve trainer's workload (invalid input data)
    Given an authorized user and given the trainerUsername, trainingYear "2025" and trainingMonth "13"
    When the user sends a GET request to retrieve trainer's workload
    Then the response status for getting trainer's workload should be 400
    And the response for getting trainer's workload should contain the error message "Month must be between 1 and 12"