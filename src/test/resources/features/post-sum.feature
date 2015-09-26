Tags: @non-visual

Feature: A feature to test adding two numbers via post

Scenario: A scenario where we add two numbers via post
  Given I am creating an API POST call to add two numbers
    With data 'first' and value '1'
    With data 'second' and value '2'
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON object
    Containing a valid sum result