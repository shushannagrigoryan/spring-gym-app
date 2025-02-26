Feature: Retrieve all training types

  @createUser
  @auth
  Scenario: Successfully retrieve training types with a valid JWT
    Given the user has a valid JWT token
    When the user sends a GET request to get training types with the JWT token
    Then the getTrainingTypes response status should be 200
    And the response should contain a list of training types and message "Successfully retrieved training types."

  Scenario: Failure for retrieving training types with missing jwtToken.
    Given the user does not have a JWT token
    When the user sends a GET request to get training types without the JWT token
    Then the getTrainingTypes response status should be 401
    And the get trainingTypes response should contain an error message "Authentication failed: Full authentication is required to access this resource"
