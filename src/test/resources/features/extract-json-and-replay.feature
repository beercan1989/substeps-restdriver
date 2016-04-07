Tags: @non-visual

Feature: A feature to test extraction and replaying extracted data

Scenario: Able to extract from json and replay in a url for the next request

    RestRequest setup new request
    RestRequest perform 'POST' on '/customer/pins'

    AssertRestResponse has code '201'