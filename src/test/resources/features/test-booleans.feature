Tags: @non-visual

Feature: A feature to test getting booleans

Scenario Outline: A scenario where we get a <value> result
  Given I am creating an API GET call to get a '<value>' boolean
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON object
    Containing a valid boolean result of <value>

Examples:
  |value|
  |true |
  |false|