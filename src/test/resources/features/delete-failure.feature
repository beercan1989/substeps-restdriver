Tags: @non-visual

Feature: A feature to test deleting stuff

Scenario: A scenario where we delete stuff
    Given I am creating an API call to delete stuff, but setup to fail
    And I get a not found response
