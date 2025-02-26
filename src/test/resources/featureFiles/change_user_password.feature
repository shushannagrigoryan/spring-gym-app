Feature: Change user password

  @createUser
  @auth
  Scenario: Successfully changing user's password
    Given a user with valid username and password
    When the user submits a put request to change password
    Then the changePassword response status should be 200
    And the changePassword response should contain a message "Successfully changed user password."

  @createUser
  Scenario: Authentication fails when user sends a request to change password
    Given a user with valid username and password
    When the user submits a put request without jwtToken
    Then the changePassword response status should be 401
    And the changePassword response should contain an error message "Authentication failed: Full authentication is required to access this resource"