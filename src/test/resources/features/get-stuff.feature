Tags: @non-visual

Feature: A feature to test getting stuff

Scenario: A scenario where we get stuff
  Given I am creating an API call to get some stuff
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON object
    Containing some stuff