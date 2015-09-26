Tags: @non-visual

Feature: A feature to test getting a list of strings

Scenario: A scenario where we get a list of strings
  Given I am creating an API call to get a list of strings
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON array
    Containing list of some strings