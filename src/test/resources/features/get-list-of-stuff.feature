Tags: @non-visual

Feature: A feature to test getting a list of stuff

Scenario: A scenario where we get a list of stuff
  Given I am creating an API call to get a list of stuff
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON array
    Containing list of some stuff