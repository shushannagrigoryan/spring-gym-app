Feature: User Login

  @createUser
  Scenario: Successfully login for user
    Given a user with username and password
    When the user submits a login request
    Then the login response status should be 200
    And the login response should contain a generated jwtToken

  Scenario: Failed login for invalid user
    Given a user with username "A" and password "myPassword"
    When the user submits a login request
    Then the login response status should be 401
    And the login response should contain an error message "Authentication failed: Bad credentials"