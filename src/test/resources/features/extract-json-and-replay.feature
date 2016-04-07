Tags: @non-visual

Feature: A feature to test extraction and replaying extracted data

Scenario: Able to extract from json and replay in a url for the next request
    RestRequest setup new request
    RestRequest perform 'GET' on '/generate-uuid'

    AssertRestResponse has code '200'
    AssertRestResponseBody is JSON 'object'

    ExtractJsonElement ByJsonPath 'result' as a 'string' into scenario variable 'GENERATED_UUID'

    RestRequest setup new request
    RestRequest add path param with name 'extracted-json-data' and scenario variable 'GENERATED_UUID'
    RestRequest perform 'GET' on '/replay-path-param/{extracted-json-data}'

    AssertRestResponse has code '200'
    AssertRestResponseBody is JSON 'object'

    AssertJsonElement ByJsonPath 'result' in RestResponseBody a 'string' scenario variable: GENERATED_UUID