Tags: @non-visual

Feature: A feature to test adding two numbers via get

Scenario: A scenario where we add two numbers via get
  Given I am creating an API GET call to add two numbers
  And I get a successful response
    Containing a valid JSON object
    Containing a valid sum result