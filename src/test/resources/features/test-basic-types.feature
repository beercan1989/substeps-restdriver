Tags: @non-visual

Feature: A feature to test getting types and asserting values

Scenario Outline: A scenario where we get a <value> <type> result
  Given I am creating an API GET call to get a '<value>' '<type>'
  Then I execute the API call
  And I get a successful response
    Containing a valid JSON object
    Containing a valid '<type>' result of '<value>'

Examples:
  |value|type   |
  |true |boolean|
  |false|boolean|
  |test |string |
  |500  |number |
