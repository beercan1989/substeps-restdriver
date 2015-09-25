Tags: @non-visual

Feature: A feature to test getting some stuff

Scenario: A scenario where we get some stuff
  Given I am creating an API call to get some stuff
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON array
    Containing the some stuff